package com.development.sota.scooter.ui.purse.presentation.fragments.upbalance

import android.content.Context
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.PurseUpBalanceInteractor
import com.development.sota.scooter.ui.purse.domain.PurseUpBalanceInteractorImpl
import com.development.sota.scooter.ui.purse.presentation.WalletView
import moxy.MvpPresenter

class UpBalancePresenter(val context: Context): MvpPresenter<UpBalance>(), BasePresenter {
    private val interactor: PurseUpBalanceInteractor = PurseUpBalanceInteractorImpl(this)




    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }

}