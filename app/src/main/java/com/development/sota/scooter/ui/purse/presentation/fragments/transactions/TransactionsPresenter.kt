package com.development.sota.scooter.ui.purse.presentation.fragments.transactions

import android.annotation.SuppressLint
import android.content.Context
import com.development.sota.scooter.R
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.WalletTransactionsInteractor
import com.development.sota.scooter.ui.purse.domain.WalletTransactionsInteractorImpl
import com.development.sota.scooter.ui.purse.domain.entities.TransactionModel
import moxy.MvpPresenter

class TransactionsPresenter(val context: Context) : MvpPresenter<TransactionsView>(), BasePresenter {

    private val interactor: WalletTransactionsInteractor = WalletTransactionsInteractorImpl(this)


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
    }


    fun getTransactions(card: List<TransactionModel>){
        viewState.showTransactions(card.reversed())

    }

    public fun resumeView() {
        interactor.getTransactions()
        viewState.setLoading(true)
    }

    @SuppressLint("TimberArgCount")
    fun errorGotFromServer(error: String) {

        viewState.showToast(context.getString(R.string.error_api))
        viewState.setLoading(false)
    }

    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }
}