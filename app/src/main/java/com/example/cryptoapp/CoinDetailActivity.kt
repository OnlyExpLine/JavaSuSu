package com.example.cryptoapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.example.cryptoapp.databinding.ActivityCoinDetailBinding

class CoinDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinViewModel
    private lateinit var binding: ActivityCoinDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!intent.hasExtra(EXTRA_FROM_SYMBOL)) {
            finish()
            return
        }

        val fromSymbol = intent.getStringExtra(EXTRA_FROM_SYMBOL)

        // Используем фабрику для создания ViewModel
        val factory = CoinViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[CoinViewModel::class.java]

        fromSymbol?.let {
            viewModel.getDetailInfo(it).observe(this, Observer { detailInfo ->
                // Теперь используем binding для доступа к элементам интерфейса
                binding.tvPrice.text = detailInfo.price
                binding.tvMinPrice.text = detailInfo.lowDay
                binding.tvMaxPrice.text = detailInfo.highDay
                binding.tvLastMarket.text = detailInfo.lastMarket
                binding.tvLastUpdate.text = detailInfo.getFormattedTime()
                binding.tvFromSymbol.text = detailInfo.fromSymbol
                binding.tvToSymbol.text = detailInfo.toSymbol
                Picasso.get().load(detailInfo.getFullImageUrl()).into(binding.ivLogoCoin)
            })
        }
    }

    companion object {
        private const val EXTRA_FROM_SYMBOL = "fSym"

        fun newIntent(context: Context, fromSymbol: String): Intent {
            val intent = Intent(context, CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_FROM_SYMBOL, fromSymbol)
            return intent
        }
    }
}
