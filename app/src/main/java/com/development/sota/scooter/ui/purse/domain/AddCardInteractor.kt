//package com.development.sota.scooter.ui.purse.domain
//
//import com.development.sota.scooter.common.base.BaseInteractor
//import com.development.sota.scooter.db.SharedPreferencesProvider
//import com.development.sota.scooter.ui.purse.presentation.fragments.cards.AddCardPresenter
//import io.reactivex.rxjava3.disposables.CompositeDisposable
//
//
//// fragment interfaces
//interface AddCardInteractor : BaseInteractor{
//
//    fun addCard(card: String)
//
//    fun deleteCard(card: String, card_id: Long, id: Long)
//
//    fun makeCryptogram()
//
//    fun setMain(card: String, id: Long)
//
//    fun confirmCard(MD: Int, PaRes: String)
//
//    fun getCards()
//
//}
//class AddCardInteractorImpl(val presenter: AddCardPresenter) : AddCardInteractor {
//    private val compositeDisposable = CompositeDisposable()
//    private val sharedPreferences = SharedPreferencesProvider(presenter.context).sharedPreferences
//
//
//
//    override fun addCard(card: String) {
//
//    }
//
//    override fun deleteCard(card: String, card_id: Long, id: Long) {
//
//    }
//
//    override fun makeCryptogram() {
//        TODO("Not yet implemented")
//    }
//
//    override fun setMain(card: String, id: Long) {
//        TODO("Not yet implemented")
//    }
//
//    override fun confirmCard(MD: Int, PaRes: String) {
//        TODO("Not yet implemented")
//    }
//
//    override fun getCards(){
//        val clientId = sharedPreferences.getLong("id", -1).toString()
//    //    compositeDisposable.add(
//    //        PurseRetrofitProvider.service.fetchPurseInfo(
//    //            sharedPreferences.getLong("id", -1).toLong()
//    //        ).subscribeOn(Schedulers.io())
//    //        .observeOn(AndroidSchedulers.mainThread())
//    //            .forEach()    )
//
//    }
//
//    override fun disposeRequests(){
//        compositeDisposable.dispose()
//        compositeDisposable.clear()
//    }
//}