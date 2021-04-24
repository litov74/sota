package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.development.sota.scooter.R
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.WalletCardsInteractor
import com.development.sota.scooter.ui.purse.domain.WalletCardsInteractorImpl
import com.development.sota.scooter.ui.purse.domain.entities.UserCardModel
import moxy.MvpPresenter

class CardListPresenter(
    val context: Context
) : MvpPresenter<ICardList>(), BasePresenter {

    private val interactor: WalletCardsInteractor = WalletCardsInteractorImpl(this)

    fun getCards(cards: UserCardModel){
        viewState.showUserCards(cards.last_four.toString(), cards.is_main.toString())
    }


    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }

    fun progressAddCard() {

    }

    fun setCards(userCardModels: List<UserCardModel>) {
        viewState.showProgress(false)
        viewState.showCards(userCardModels)
    }

    fun resumeView() {
       interactor.getCards()
    }

    fun showLoading(boolean: Boolean) {
        viewState.showProgress(boolean)
    }

    @SuppressLint("TimberArgCount")
    fun errorGotFromServer(error: String) {
        Log.w("Error calling server", error)
        viewState.showToast(context.getString(R.string.error_api))
    }
}