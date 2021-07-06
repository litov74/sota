package com.development.sota.scooter.ui.doc

import com.development.sota.scooter.R
import com.development.sota.scooter.common.base.BasePresenter
import moxy.MvpPresenter

class LoginUserAgreementPresenter : MvpPresenter<LoginUserAgreement>(), BasePresenter {
    fun onNextClicked(currentIndex: Int) {
        if (currentIndex + 1 <= 3) {
            val index = currentIndex + 1

            if (index == 3) {
                viewState.hideNextButton()
            }

            viewState.setViewPagerPage(index)
        } else {
            viewState.closeUserAgreement()
        }
    }

    override fun onDestroyCalled() {}
}