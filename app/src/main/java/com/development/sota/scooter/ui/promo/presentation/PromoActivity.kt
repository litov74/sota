package com.development.sota.scooter.ui.promo.presentation

import android.os.Bundle
import com.development.sota.scooter.databinding.ActivityPromoBinding
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter

interface PromoView : MvpView

interface PromoActivityView

class PromoActivity : MvpAppCompatActivity(), PromoView, PromoActivityView {

    private val presenter by moxyPresenter { PromoPresenter(this) }

    private var _binding: ActivityPromoBinding? = null
    private val binding get() = _binding!!

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        _binding = ActivityPromoBinding.inflate(layoutInflater)

        binding.mainPromoImageButtonBack.setOnClickListener {
            onBackPressed()
        }

        setContentView(binding.root)
    }
}