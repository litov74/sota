package com.development.sota.scooter.ui.purse.domain

import com.development.sota.scooter.common.base.BaseInteractor
import com.development.sota.scooter.db.SharedPreferencesProvider
import com.development.sota.scooter.net.ClientRetrofitProvider
import com.development.sota.scooter.net.GetClientCardsProvider
import com.development.sota.scooter.ui.map.data.Client
import com.development.sota.scooter.ui.purse.presentation.PursePresenter
import com.development.sota.scooter.ui.purse.presentation.fragments.cards.CardsPresenter
import com.development.sota.scooter.ui.purse.presentation.fragments.transactions.TransactionsPresenter
import com.development.sota.scooter.ui.purse.presentation.fragments.upbalance.UpBalancePresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers


interface PurseTransactionsInteractor: BaseInteractor{
    fun getTransactions()
    fun getClientInfo()
}

interface PurseInteractor: BaseInteractor{
    fun getClientCash()
    fun getClientInfo()
}

interface PurseCardsInteractor: BaseInteractor{
    fun getClientCards()
    fun setCardMain()
    fun addCard()
    fun removeCard()
    fun getClientInfo()
}

interface PurseUpBalanceInteractor: BaseInteractor{
    fun loadCards()
}

class PurseInteractorImpl(val presenter: PursePresenter): PurseInteractor  {
    private val compositeDisposable = CompositeDisposable()
    private val sharedPreferences = SharedPreferencesProvider(presenter.context).sharedPreferences
    public val k = sharedPreferences.getString("id", null)!!

    override fun getClientCash() {

    }

    override fun getClientInfo() {

    }

    override fun disposeRequests() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }


}

class PurseCardsInteractorImpl(val presenter: CardsPresenter): PurseCardsInteractor{
    private val compositeDisposable = CompositeDisposable()
    override fun getClientCards() {
        TODO("Not yet implemented")
    }

    override fun setCardMain() {
        TODO("Not yet implemented")
    }

    override fun addCard() {
        TODO("Not yet implemented")
    }

    override fun removeCard() {
        TODO("Not yet implemented")
    }

    override fun getClientInfo() {
        TODO("Not yet implemented")
    }

    override fun disposeRequests() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

}

class PurseUpBalanceInteractorImpl(val presenter: UpBalancePresenter): PurseUpBalanceInteractor{
    private val compositeDisposable = CompositeDisposable()
    override fun loadCards() {
        TODO("Not yet implemented")
    }

    override fun disposeRequests() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

}

class PurseTransactionsInteractorImpl(val presenter: TransactionsPresenter): PurseTransactionsInteractor{
    private val compositeDisposable = CompositeDisposable()
    override fun getTransactions() {
        TODO("Not yet implemented")
    }

    override fun getClientInfo() {
        TODO("Not yet implemented")
    }

    override fun disposeRequests() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

}