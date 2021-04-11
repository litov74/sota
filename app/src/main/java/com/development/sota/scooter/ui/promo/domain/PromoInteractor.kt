package com.development.sota.scooter.ui.promo.domain

import com.development.sota.scooter.common.base.BaseInteractor
import com.development.sota.scooter.net.GetReplenishmentPackagesProvider
import com.development.sota.scooter.ui.promo.presentation.PromoPresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

interface PromoInteractor: BaseInteractor {
    fun getAllPromos()
}

class PromoInteractorImpl(private val presenter: PromoPresenter): PromoInteractor{
    private val compositeDisposable = CompositeDisposable()
    override fun getAllPromos() {
        //TODO: Need promos
    }
    override fun disposeRequests() {
        compositeDisposable.dispose()
    }
}