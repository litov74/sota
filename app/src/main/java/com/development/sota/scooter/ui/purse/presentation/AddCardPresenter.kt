package com.development.sota.scooter.ui.purse.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.development.sota.scooter.R
import com.development.sota.scooter.common.Constants
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.WalletAddCardsInteractorImpl
import com.development.sota.scooter.ui.purse.domain.WalletCardsInteractor
import com.development.sota.scooter.ui.purse.domain.entities.CardPaymentVerificationModel
import com.development.sota.scooter.ui.purse.domain.entities.UserCardModel
import com.development.sota.scooter.ui.purse.domain.entities.WrapperCardPaymentVerificationModel
import com.google.gson.Gson
import moxy.MvpPresenter
import ru.cloudpayments.sdk.card.Card
import java.lang.Exception


class AddCardPresenter(val context: Context) : MvpPresenter<IAddCardView>(), BasePresenter {
    private val interactor: WalletCardsInteractor = WalletAddCardsInteractorImpl(this)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
    }


    @SuppressLint("TimberArgCount")
    fun errorGotFromServer(error: String) {
        Log.w("Error calling server", error)
        viewState.showToast(context.getString(R.string.error_api))
    }

    fun showError(string: String) {
        viewState.showToast(string)
    }

    fun attachCardError() {
        viewState.setLoading(false)
            viewState.showToast("Пожалуйста, проверьте данные карты")
    }

    fun attachCardDone(wrapperCardPaymentVerificationModel: WrapperCardPaymentVerificationModel) {

        if (wrapperCardPaymentVerificationModel.Success) {
            viewState.finish()
        } else {
            try {
                val article = Gson().fromJson(wrapperCardPaymentVerificationModel.Model, CardPaymentVerificationModel::class.java)
                val parReq = article.PaReq
                viewState.show3dSecure(article.AcsUrl, article.TransactionId , parReq)
            } catch (exception:Exception) {
                exception.printStackTrace()
            }

        }

    }

    fun success3DSecure() {
        viewState.setLoading(false)
        viewState.finish()
    }

    fun showProgress(boolean: Boolean) {
        viewState.setLoading(boolean)
    }

     fun success3dSecure(md: String, paRes: String) {
       interactor.confirmCard(md, paRes)
    }

    fun error3dSecure() {
        viewState.showToast("3D Secure произошла ошибка")
       viewState.setLoading(false)
    }

    fun attachCard(cardNumber: String, cardDate: String, cardCVV: String) {
        if (Card.isValidNumber(cardNumber) && Card.isValidExpDate(cardDate)) {
            val cardCryptogram = Card.cardCryptogram(cardNumber.trim(),cardDate.replace("/","").trim(), cardCVV.trim(), Constants.MERCHANT_PUBLIC_ID)

            if(cardCryptogram == null){
                viewState.showToast("Пожалуйста, проверьте данные карты")
            }else{
                System.out.println("crypto "+cardCryptogram)
                interactor.addCard(cardCryptogram)
            }
        } else {
            viewState.showToast("Пожалуйста, проверьте данные карты")
        }
    }

    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }

     fun setMain(userCardModel: UserCardModel) {

    }
}