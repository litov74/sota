package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import android.content.Context
import com.development.sota.scooter.common.Constants
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.WalletAddCardsInteractorImpl
import com.development.sota.scooter.ui.purse.domain.WalletCardsInteractor
import com.development.sota.scooter.ui.purse.domain.WalletCardsInteractorImpl
import moxy.MvpPresenter
import ru.cloudpayments.sdk.card.Card

class AddCardPresenter(val context: Context): MvpPresenter<AddCardActivity>(), BasePresenter {

    private val interactor: WalletCardsInteractor = WalletAddCardsInteractorImpl(this)

    fun progressAttachCard() {
         viewState.showProgress(true)
    }

    fun showError(error: String) {
        viewState.showToast(error)
    }

    fun attachCardDone() {

    }


    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }

    fun attachCard(cardNumber: String, cardDate: String, cardCVV: String) {
        if (Card.isValidNumber(cardNumber) && Card.isValidExpDate(cardDate)) {
            val cardCryptogram = Card.cardCryptogram(cardNumber,cardDate, cardCVV, Constants.MERCHANT_PUBLIC_ID)
            if(cardCryptogram == null){
                viewState.showToast("Пожалуйста, проверьте данные карты")
            }else{
                interactor.addCard(cardCryptogram)
            }
        } else {
            viewState.showToast("Пожалуйста, проверьте данные карты")
        }
    }



}