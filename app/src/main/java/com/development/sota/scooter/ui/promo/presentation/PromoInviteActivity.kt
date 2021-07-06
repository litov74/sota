package com.development.sota.scooter.ui.promo.presentation

import android.content.Intent
import android.os.Bundle
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.ActivityInvitePromoBinding
import com.development.sota.scooter.databinding.ActivityPromoBinding
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter

interface PromoInviteView : MvpView

interface PromoInviteActivityView

class PromoInviteActivity : MvpAppCompatActivity(), PromoInviteView, PromoInviteActivityView {

    private val presenter by moxyPresenter { PromoInvitePresenter(this) }

    private var _binding: ActivityInvitePromoBinding? = null
    private val binding get() = _binding!!

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        _binding = ActivityInvitePromoBinding.inflate(layoutInflater)

        binding.mainPromoImageButtonBack.setOnClickListener {
            onBackPressed()
        }
        binding.buttonInvite.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Привет! \u2028Скачай мобильное приложение \n" +
                    "экспресс-аренды электросамокатов\n" +
                    "\n" +
                    "под Android https://play.google.com/store/apps/details?id=com.development.sota.scooter\n" +
                    "или iOS https://apps.apple.com/ru/app/sota-%D0%B0%D1%80%D0%B5%D0%BD%D0%B4%D0%B0-%D1%8D%D0%BB%D0%B5%D0%BA%D1%82%D1%80%D0%BE%D1%81%D0%B0%D0%BC%D0%BE%D0%BA%D0%B0%D1%82%D0%BE%D0%B2/id1562820981\n" +
                    "\n" +
                    "Используй код Sota\n" +
                    "\n" +
                    "и получи бонусы на свой счет! ");
            startActivity(Intent.createChooser(shareIntent, getString(R.string.send_to)))
        }

        setContentView(binding.root)
    }
}