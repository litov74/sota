package com.development.sota.scooter.ui.purse.presentation

import android.content.Context
import android.util.Log
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.PurseInteractorImpl
import moxy.MvpPresenter

class PursePresenter(val context: Context) : MvpPresenter<WalletView>(){
    private val PurseUpBalanceInteractor = PurseInteractorImpl(this)

}