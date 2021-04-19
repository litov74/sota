package com.development.sota.scooter.ui.purse.domain

import android.content.Context
import com.development.sota.scooter.MainActivity
import com.development.sota.scooter.common.base.BaseInteractor
import com.development.sota.scooter.db.SharedPreferencesProvider
import com.development.sota.scooter.net.ClientRetrofitProvider
import com.development.sota.scooter.net.PurseRetrofitProvider
import com.development.sota.scooter.ui.purse.domain.entities.Card
import com.development.sota.scooter.ui.purse.presentation.WalletPresenter
import com.development.sota.scooter.ui.purse.presentation.fragments.cards.AddCardPresenter
import com.development.sota.scooter.ui.purse.presentation.fragments.cards.CardsPresenter
import com.development.sota.scooter.ui.purse.presentation.fragments.transactions.TransactionsPresenter
import com.development.sota.scooter.ui.purse.presentation.fragments.upbalance.UpBalancePresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.Schedulers.io
import javax.security.auth.callback.Callback

interface WalletInteractor : BaseInteractor{

    fun getUserBalance()
} //main interface


// fragment interfaces
interface WalletCardsInteractor : BaseInteractor{

    fun addCard(card: String, id: Long)

    fun deleteCard(card: String, card_id: Long, id: Long)

    fun makeCryptogram()

    fun setMain(card: String, id: Long)

    fun confirmCard(MD: Int, PaRes: String)

    fun getCards()

}

interface WalletUpBalanceInteractor : BaseInteractor{

    fun upBalance()
}

interface WalletTransactionsInteractor : BaseInteractor{

    fun getTransactions()

}
// End of interfaces




class WalletInteractorImpl(val presenter: WalletPresenter) : WalletInteractor {
    private val compositeDisposable = CompositeDisposable()
    private val sharedPreferences = SharedPreferencesProvider(presenter.context).sharedPreferences


    override fun getUserBalance() {
        compositeDisposable.add(
            ClientRetrofitProvider.service.getClient(
                sharedPreferences.getLong("id", -1).toString()
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        presenter.getUserBalance(
                            it[0]
                        )
                    },
                    onError = { presenter.errorGotFromServer(it.localizedMessage) }
                )
        )
    }

    override fun disposeRequests(){
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}

class WalletCardsInteractorImpl(val presenter: CardsPresenter) : WalletCardsInteractor {
    private val compositeDisposable = CompositeDisposable()
    private val sharedPreferences = SharedPreferencesProvider(presenter.context).sharedPreferences

    override fun addCard(card: String, id: Long) {

    }

    override fun deleteCard(card: String, card_id: Long, id: Long) {

    }

    override fun makeCryptogram() {
        TODO("Not yet implemented")
    }

    override fun setMain(card: String, id: Long) {
        TODO("Not yet implemented")
    }

    override fun confirmCard(MD: Int, PaRes: String) {
        TODO("Not yet implemented")
    }

    override fun getCards(){
        val clientId = sharedPreferences.getLong("id", -1).toString()
        compositeDisposable.add(
            PurseRetrofitProvider.service.fetchPurseInfo(
                sharedPreferences.getLong("id", -1).toLong()
            ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
                .forEach()    )






    }



    override fun disposeRequests(){
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}

class WalletUpBalanceInteractorImpl(val presenter: UpBalancePresenter) : WalletUpBalanceInteractor {
    private val compositeDisposable = CompositeDisposable()
    override fun upBalance() {
        TODO("Not yet implemented")
    }

    override fun disposeRequests(){
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}

class WalletTransactionsInteractorImpl(val presenter: TransactionsPresenter) : WalletTransactionsInteractor {
    private val compositeDisposable = CompositeDisposable()
    private val sharedPreferences = SharedPreferencesProvider(presenter.context).sharedPreferences
    override fun getTransactions() {
        val clientId = sharedPreferences.getLong("id", -1).toString()
        compositeDisposable.add(
            PurseRetrofitProvider.service.fetchTransactionsInfo(
                sharedPreferences.getLong("id", -1).toLong()
            ).subscribeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        presenter.getTransactions()
                    },
                    onError = {presenter.errorGotFromServer(it.localizedMessage)}
                ))
    }

    override fun disposeRequests(){
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}

class WalletAddCardsInteractorImpl(val presenter: AddCardPresenter) : WalletCardsInteractor {
    private val compositeDisposable = CompositeDisposable()
    private val sharedPreferences = SharedPreferencesProvider(presenter.context).sharedPreferences

    override fun addCard(card: String, id: Long) {

    }

    override fun deleteCard(card: String, card_id: Long, id: Long) {

    }

    override fun makeCryptogram() {
        TODO("Not yet implemented")
    }

    override fun setMain(card: String, id: Long) {
        TODO("Not yet implemented")
    }

    override fun confirmCard(MD: Int, PaRes: String) {
        TODO("Not yet implemented")
    }

    override fun getCards(){
        TODO("Not to be implemented")
    }



    override fun disposeRequests(){
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}