package com.development.sota.scooter.ui.purse.presentation.fragments.upbalance

import android.content.Context
import android.util.Log
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.WalletUpBalanceInteractor
import com.development.sota.scooter.ui.purse.domain.WalletUpBalanceInteractorImpl
import com.development.sota.scooter.ui.purse.domain.entities.UpBalancePackageModel
import moxy.MvpPresenter

class UpBalancePresenter(val context: Context) : MvpPresenter<UpBalanceView>(), BasePresenter {

    private val interactor: WalletUpBalanceInteractor = WalletUpBalanceInteractorImpl(this)


    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }

    fun resumeView() {
        getUpBalancePackages()
    }

    fun getUpBalancePackages() {
        interactor.getReplenishmentPackages();
    }

    fun showProgress(boolean: Boolean) {
        viewState.showProgress(boolean)
    }

    fun upBalancePackage(id: Long) {
        interactor.upBalance(id)
    }

    fun balanceUpdated() {
        viewState.updateUserBalance()
        getUpBalancePackages()
    }

    fun setUpBalancePackages(list: List<UpBalancePackageModel>) {
        viewState.showProgress(false)
        viewState.setListPackages(list)
    }

}