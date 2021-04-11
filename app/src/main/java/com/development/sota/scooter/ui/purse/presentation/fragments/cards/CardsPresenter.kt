package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import android.content.Context
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.profile.domain.ProfileInteractorImpl
import com.development.sota.scooter.ui.purse.domain.PurseCardsInteractor
import com.development.sota.scooter.ui.purse.domain.PurseCardsInteractorImpl
import moxy.MvpPresenter

class CardsPresenter(val context: Context): MvpPresenter<CardsView>(), BasePresenter{
    private val interactor: PurseCardsInteractor = PurseCardsInteractorImpl(this)

    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }


}