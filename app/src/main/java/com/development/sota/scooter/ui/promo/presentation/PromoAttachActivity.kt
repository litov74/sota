package com.development.sota.scooter.ui.promo.presentation

import android.content.Intent
import android.os.Bundle
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.ActivityAttachPromoBinding
import com.development.sota.scooter.databinding.ActivityPromoBinding
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter

interface PromoAttachView : MvpView

interface PromoAttachActivityView

class PromoAttachActivity : MvpAppCompatActivity(), PromoAttachView, PromoAttachActivityView {

    private val presenter by moxyPresenter { PromoAttachPresenter(this) }

    private var _binding: ActivityAttachPromoBinding? = null
    private val binding get() = _binding!!

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        _binding = ActivityAttachPromoBinding.inflate(layoutInflater)
        binding.mainPromoImageButtonBack.setOnClickListener {
            onBackPressed()
        }


        setContentView(binding.root)
    }
}