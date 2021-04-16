package com.development.sota.scooter.ui.profile.domain

import com.development.sota.scooter.common.base.BaseInteractor
import com.development.sota.scooter.db.SharedPreferencesProvider
import com.development.sota.scooter.net.ClientRetrofitProvider
import com.development.sota.scooter.ui.profile.domain.entities.ClientUpdateNameData
import com.development.sota.scooter.ui.profile.domain.entities.ClientUpdateNamePhone
import com.development.sota.scooter.ui.profile.presentation.ProfilePresenter
import com.google.gson.JsonObject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers


interface ProfileInteractor : BaseInteractor {
    fun getProfileInfo()
    fun setProfileInfo(clientName: String, phone: String)
}

class ProfileInteractorImpl(private val presenter: ProfilePresenter) : ProfileInteractor {

    private val compositeDisposable = CompositeDisposable()
    private val sharedPreferences = SharedPreferencesProvider(presenter.context).sharedPreferences


    override fun getProfileInfo() {
        compositeDisposable.add(
            ClientRetrofitProvider.service
                .getClient(sharedPreferences.getLong("id", -1).toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = { presenter.showError(it.localizedMessage ?: "") },
                    onNext = {
                        presenter.gotClientsFromAPI(it)
                    }
                )
        )
    }


    override fun setProfileInfo(clientName: String, phone: String) {
        val clientId = sharedPreferences.getLong("id", -1)

        ClientRetrofitProvider.service
            .updateClientName(ClientUpdateNameData(clientId, clientName))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                presenter.showLoading(true)
            }
            .subscribeBy(
                onError = {
                    presenter.showLoading(false)
                    presenter.showError(it.localizedMessage ?: "") },
                onNext = {
                    ClientRetrofitProvider.service
                        .updateClientPhone(ClientUpdateNamePhone(clientId, phone))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onError = {
                                presenter.showLoading(false)
                                presenter.showError(it.localizedMessage ?: "") },
                            onNext = {
                                presenter.completeUpdateProfile()
                            }
                        )
                }
            )

    }



    override fun disposeRequests() {
        compositeDisposable.clear()
    }
}