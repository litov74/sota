package com.development.sota.scooter.ui.purse.presentation.fragments.upbalance

import android.content.Context
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.WalletUpBalanceInteractor
import com.development.sota.scooter.ui.purse.domain.WalletUpBalanceInteractorImpl
import moxy.MvpPresenter

class UpBalancePresenter(private val context: Context) : MvpPresenter<UpBalanceView>(), BasePresenter {

    private val interactor: WalletUpBalanceInteractor = WalletUpBalanceInteractorImpl(this)


    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }
}