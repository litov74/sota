package com.development.sota.scooter.ui.purse.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.development.sota.scooter.R
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.map.data.Client
import com.development.sota.scooter.ui.purse.domain.WalletInteractor
import com.development.sota.scooter.ui.purse.domain.WalletInteractorImpl
import moxy.MvpPresenter

class WalletPresenter(val context: Context) : MvpPresenter<WalletView>(), BasePresenter {
    private val interactor: WalletInteractor = WalletInteractorImpl(this)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        interactor.getUserBalance()
        viewState.setLoading(true)
    }

    fun getUserBalance(client: Client) {
        viewState.setUserBalance(client.balance)
        viewState.setLoading(false)
    }

    @SuppressLint("TimberArgCount")
    fun errorGotFromServer(error: String) {
        Log.w("Error calling server", error)
        viewState.showToast(context.getString(R.string.error_api))
        viewState.setLoading(false)
    }

    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }
}