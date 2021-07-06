package com.development.sota.scooter.ui.doc

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.development.sota.scooter.databinding.ActivityMapBinding
import com.development.sota.scooter.databinding.FragmentDocUserAgreementBinding
import com.development.sota.scooter.databinding.FragmentLoginUserAgreementBinding
import com.development.sota.scooter.ui.login.presentation.LoginActivityView
import moxy.MvpAppCompatActivity
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd

interface LoginUserAgreement : MvpView {
    @AddToEnd
    fun changeConfirmButtonText(resId: Int)

    @AddToEnd
    fun setViewPagerPage(index: Int)

    @AddToEnd
    fun closeUserAgreement()

    @AddToEnd
    fun hideNextButton()
}

class LoginUserAgreementActivity : MvpAppCompatActivity(), LoginUserAgreement {
    private val presenter by moxyPresenter { LoginUserAgreementPresenter() }

    private var _binding: FragmentDocUserAgreementBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding =  FragmentDocUserAgreementBinding.inflate(layoutInflater)

        binding.viewPagerLoginAgreement.isUserInputEnabled = false
        binding.viewPagerLoginAgreement.adapter = LoginUserAgreementAdapter()
        binding.buttonCloseDoc.setOnClickListener { finish() }
        binding.buttonLoginUserAgreementConfirm.setOnClickListener { presenter.onNextClicked(binding.viewPagerLoginAgreement.currentItem) }
        setContentView(binding.root)

    }

    override fun changeConfirmButtonText(resId: Int) {
        runOnUiThread {
            binding.buttonLoginUserAgreementConfirm.setText(resId)
        }
    }

    override fun setViewPagerPage(index: Int) {
        runOnUiThread {
            binding.viewPagerLoginAgreement.currentItem = index
        }
    }

    override fun closeUserAgreement() {
        finish()
    }

    override fun hideNextButton() {
        binding.buttonLoginUserAgreementConfirm.visibility = View.GONE
    }
}