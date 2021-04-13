package com.development.sota.scooter.ui.purse.presentation.fragments.transactions

import android.content.Context
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.WalletTransactionsInteractor
import com.development.sota.scooter.ui.purse.domain.WalletTransactionsInteractorImpl
import com.development.sota.scooter.ui.purse.domain.WalletUpBalanceInteractor
import com.development.sota.scooter.ui.purse.domain.WalletUpBalanceInteractorImpl
import moxy.MvpPresenter

class TransactionsPresenter(private val context: Context) : MvpPresenter<TransactionsView>(), BasePresenter {

    private val interactor: WalletTransactionsInteractor = WalletTransactionsInteractorImpl(this)

    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }
}