package com.development.sota.scooter.ui.purse.domain

import android.content.SharedPreferences
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.presentation.fragments.cards.CardsPresenter
import io.reactivex.rxjava3.disposables.CompositeDisposable

interface WalletViewInteractor {

}



class WalletCardsViewInteractor(val presenter: CardsPresenter) : WalletViewInteractor {
    private val compositeDisposable = CompositeDisposable()
}