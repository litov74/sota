package com.development.sota.scooter.ui.purse.presentation.fragments.transactions

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.development.sota.scooter.R
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.WalletTransactionsInteractor
import com.development.sota.scooter.ui.purse.domain.WalletTransactionsInteractorImpl
import com.development.sota.scooter.ui.purse.domain.WalletUpBalanceInteractor
import com.development.sota.scooter.ui.purse.domain.WalletUpBalanceInteractorImpl
import com.development.sota.scooter.ui.purse.domain.entities.Card
import moxy.MvpPresenter

class TransactionsPresenter(val context: Context) : MvpPresenter<TransactionsView>(), BasePresenter {

    private val interactor: WalletTransactionsInteractor = WalletTransactionsInteractorImpl(this)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        interactor.getTransactions()
        viewState.setLoading(true)
    }


    fun getTransactions(card: Card){
        viewState.showTransactions(card.first_six.toString(), card.last_four.toString(), card.dateTime.toString(), card.cost.toString())

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