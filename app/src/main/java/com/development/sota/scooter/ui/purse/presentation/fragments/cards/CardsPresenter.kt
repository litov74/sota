package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import android.content.Context
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.WalletCardsViewInteractor
import com.development.sota.scooter.ui.purse.domain.WalletViewInteractor
import moxy.MvpPresenter

class CardsPresenter(
    private val context: Context
) : MvpPresenter<CardsView>(), BasePresenter {

    private val interactor: WalletViewInteractor = WalletCardsViewInteractor(this)


    override fun onDestroyCalled() {
        TODO("Not yet implemented")
    }

}