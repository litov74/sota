package com.development.sota.scooter.ui.promo.presentation

import android.content.Intent
import android.os.Bundle
import com.development.sota.scooter.databinding.ActivityPromoBinding
import com.development.sota.scooter.ui.help.presentation.HelpActivity
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd

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
        binding.buttonInvite.setOnClickListener({
            val intent = Intent (getApplicationContext(), PromoInviteActivity::class.java)
            startActivity(intent)
        })

        binding.buttonLook.setOnClickListener({
            val intent = Intent (getApplicationContext(), PromoListActivity::class.java)
            startActivity(intent)
        })



        setContentView(binding.root)
    }
}