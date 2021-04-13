package com.development.sota.scooter.ui.purse.presentation

import android.content.Context
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.WalletInteractor
import com.development.sota.scooter.ui.purse.domain.WalletInteractorImpl
import moxy.MvpPresenter

class WalletPresenter(val context: Context) : MvpPresenter<WalletView>(), BasePresenter {
    private val interactor: WalletInteractor = WalletInteractorImpl(this)



    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }
}