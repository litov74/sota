package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import android.content.Context
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.WalletCardsInteractor
import com.development.sota.scooter.ui.purse.domain.WalletCardsInteractorImpl
import com.development.sota.scooter.ui.purse.domain.WalletInteractor
import moxy.MvpPresenter

class CardsPresenter(
    private val context: Context
) : MvpPresenter<CardsView>(), BasePresenter {

    private val interactor: WalletCardsInteractor = WalletCardsInteractorImpl(this)



    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }

}