package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import android.content.Context
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.WalletAddCardsInteractorImpl
import com.development.sota.scooter.ui.purse.domain.WalletCardsInteractor
import com.development.sota.scooter.ui.purse.domain.WalletCardsInteractorImpl
import moxy.MvpPresenter

class AddCardPresenter(val context: Context): MvpPresenter<AddCardActivity>(), BasePresenter {

    private val interactor: WalletCardsInteractor = WalletAddCardsInteractorImpl(this)


    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }


}