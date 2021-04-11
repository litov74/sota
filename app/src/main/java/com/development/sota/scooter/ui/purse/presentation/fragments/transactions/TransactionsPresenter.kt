package com.development.sota.scooter.ui.purse.presentation.fragments.transactions

import android.content.Context
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.PurseTransactionsInteractor
import com.development.sota.scooter.ui.purse.domain.PurseTransactionsInteractorImpl
import moxy.MvpPresenter

class TransactionsPresenter(val context: Context): MvpPresenter<TransactionsView>(), BasePresenter {
    private val interactor: PurseTransactionsInteractor = PurseTransactionsInteractorImpl(this)



    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }
}