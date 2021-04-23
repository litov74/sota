package com.development.sota.scooter.ui.purse.domain

import com.development.sota.scooter.common.base.BaseInteractor
import com.development.sota.scooter.db.SharedPreferencesProvider
import com.development.sota.scooter.net.ClientRetrofitProvider
import com.development.sota.scooter.net.PurseRetrofitProvider
import com.development.sota.scooter.ui.purse.domain.entities.AddCardModel
import com.development.sota.scooter.ui.purse.domain.entities.ConfirmCardLinkingModel
import com.development.sota.scooter.ui.purse.presentation.AddCardPresenter
import com.development.sota.scooter.ui.purse.presentation.WalletPresenter
import com.development.sota.scooter.ui.purse.presentation.fragments.cards.CardListPresenter
import com.development.sota.scooter.ui.purse.presentation.fragments.transactions.TransactionsPresenter
import com.development.sota.scooter.ui.purse.presentation.fragments.upbalance.UpBalancePresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

interface WalletInteractor : BaseInteractor{

    fun getUserBalance()
} //main interface


// fragment interfaces
interface WalletCardsInteractor : BaseInteractor{

    fun addCard(card: String)

    fun deleteCard(card: String, card_id: Long, id: Long)

    fun makeCryptogram()

    fun setMain(card: String, id: Long)

    fun confirmCard(MD: String, PaRes: String)

    fun getCards()

}

interface WalletUpBalanceInteractor : BaseInteractor{

    fun upBalance(packageId: Long)

    fun getReplenishmentPackages()
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

class WalletCardsInteractorImpl(val presenter: CardListPresenter) : WalletCardsInteractor {
    private val compositeDisposable = CompositeDisposable()
    private val sharedPreferences = SharedPreferencesProvider(presenter.context).sharedPreferences



    override fun addCard(card: String) {

    }

    override fun deleteCard(card: String, card_id: Long, id: Long) {

    }

    override fun makeCryptogram() {
        TODO("Not yet implemented")
    }

    override fun setMain(card: String, id: Long) {
        TODO("Not yet implemented")
    }

    override fun confirmCard(MD: String, PaRes: String) {

    }

    override fun getCards(){
        val clientId = sharedPreferences.getLong("id", -1)
        compositeDisposable.add(
                PurseRetrofitProvider.service.getClientCards(clientId)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe {
                            presenter.showLoading(true)
                        }
                        .subscribeBy(
                                onNext = {
                                    presenter.setCards(it)
                                },
                                onError = {
                                    presenter.errorGotFromServer(it.localizedMessage)
                                }))
    }



    override fun disposeRequests(){
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}

class WalletUpBalanceInteractorImpl(val presenter: UpBalancePresenter) : WalletUpBalanceInteractor {
    private val compositeDisposable = CompositeDisposable()
    private val sharedPreferences = SharedPreferencesProvider(presenter.context).sharedPreferences

    override fun upBalance(upBalance: Long) {
        compositeDisposable.add(
            PurseRetrofitProvider.service.topUpBalance(sharedPreferences.getLong("id", -1),upBalance)
                .doOnSubscribe({presenter.showProgress(true)})
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        presenter.balanceUpdated()
                    },
                    onError = {
                        it.printStackTrace()
                    }
                ))
    }

    override fun getReplenishmentPackages() {
        compositeDisposable.add(
                PurseRetrofitProvider.service.getReplenishmentPackages()
                        .doOnSubscribe({presenter.showProgress(true)})
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onNext = {
                                    it.body()?.let { it1 -> presenter.setUpBalancePackages(it1) }
                                },
                                onError = {
                                    it.printStackTrace()
                                }
                        ))
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
                        presenter.getTransactions(it)
                    },
                    onError = {
                        presenter.errorGotFromServer(it.localizedMessage)
                    }
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

    override fun addCard(card: String) {
        compositeDisposable.add(
            ClientRetrofitProvider.service
                    .addClientCard(AddCardModel(sharedPreferences.getLong("id", -1),card))
                    .doOnSubscribe({
                        presenter.showProgress(true)
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onError = {
                                presenter.showError(it.localizedMessage ?: "") },
                            onNext = {
                                presenter.attachCardDone(it)

                            }

                    )
        )
    }

    override fun deleteCard(card: String, card_id: Long, id: Long) {

    }

    override fun makeCryptogram() {
        TODO("Not yet implemented")
    }

    override fun setMain(card: String, id: Long) {
        TODO("Not yet implemented")
    }

    override fun confirmCard(MD: String, PaRes: String) {
        compositeDisposable.add(
                ClientRetrofitProvider.service
                        .confirmCardClient(ConfirmCardLinkingModel(MD, PaRes))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onError = {
                                    presenter.showError(it.localizedMessage ?: "") },
                                onNext = {

                                }

                        )
        )
    }

    override fun getCards(){
        TODO("Not to be implemented")
    }



    override fun disposeRequests(){
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}