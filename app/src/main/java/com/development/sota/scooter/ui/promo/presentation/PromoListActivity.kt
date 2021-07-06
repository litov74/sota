package com.development.sota.scooter.ui.promo.presentation

import android.content.Intent
import android.os.Bundle
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.ActivityListPromoBinding
import com.development.sota.scooter.databinding.ActivityPromoBinding
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter

interface PromoListView : MvpView

interface PromoListActivityView

class PromoListActivity : MvpAppCompatActivity(), PromoListView, PromoListActivityView {

    private val presenter by moxyPresenter { PromoListPresenter(this) }

    private var _binding: ActivityListPromoBinding? = null
    private val binding get() = _binding!!

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        _binding = ActivityListPromoBinding.inflate(layoutInflater)

        binding.mainPromoImageButtonBack.setOnClickListener {
            onBackPressed()
        }

        binding.buttonAttachPromo.setOnClickListener {
            val intent = Intent (getApplicationContext(), PromoAttachActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }
}