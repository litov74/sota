package com.development.sota.scooter.ui.purse.domain

import android.content.SharedPreferences
import com.development.sota.scooter.common.base.BaseInteractor
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.db.SharedPreferencesProvider
import com.development.sota.scooter.ui.purse.presentation.WalletPresenter
import com.development.sota.scooter.ui.purse.presentation.fragments.cards.CardsPresenter
import com.development.sota.scooter.ui.purse.presentation.fragments.transactions.TransactionsPresenter
import com.development.sota.scooter.ui.purse.presentation.fragments.upbalance.UpBalancePresenter
import io.reactivex.rxjava3.disposables.CompositeDisposable

interface WalletInteractor : BaseInteractor{

    fun getUserBalance()
} //main interface


// fragment interfaces
interface WalletCardsInteractor : BaseInteractor{

    fun addCard()

    fun deleteCard()

    fun makeCryptogram()

    fun setMain()

    fun confirmCard()

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
    private val sharedPreferencesProvider = SharedPreferencesProvider(presenter.context).sharedPreferences


    override fun getUserBalance() {
        TODO("Not yet implemented")
    }

    override fun disposeRequests(){
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}

class WalletCardsInteractorImpl(val presenter: CardsPresenter) : WalletCardsInteractor {
    private val compositeDisposable = CompositeDisposable()
    override fun addCard() {
        TODO("Not yet implemented")
    }

    override fun deleteCard() {
        TODO("Not yet implemented")
    }

    override fun makeCryptogram() {
        TODO("Not yet implemented")
    }

    override fun setMain() {
        TODO("Not yet implemented")
    }

    override fun confirmCard() {
        TODO("Not yet implemented")
    }

    override fun getCards() {
        TODO("Not yet implemented")
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
    override fun getTransactions() {
        TODO("Not yet implemented")
    }

    override fun disposeRequests(){
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}