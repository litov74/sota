package com.development.sota.scooter.ui.login.domain

import com.development.sota.scooter.common.base.BaseInteractor
import com.development.sota.scooter.db.SharedPreferencesProvider
import com.development.sota.scooter.net.LoginData
import com.development.sota.scooter.net.LoginRetrofitProvider
import com.development.sota.scooter.ui.login.presentation.LoginPresenter
import com.development.sota.scooter.ui.login.presentation.fragments.code.LoginCodePresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers


interface LoginInteractor : BaseInteractor {
    fun sendLoginRequest(phone: String, name: String)
    fun saveCredentials(phone: String, name: String, id: Long, token: String)
}

class LoginInteractorImpl(val presenter: LoginPresenter) : LoginInteractor {
    private val compositeDisposable = CompositeDisposable()
    private val sharedPreferences = SharedPreferencesProvider(presenter.context).sharedPreferences

    override fun sendLoginRequest(phone: String, name: String) {
        val loginData = LoginData(
            client_name = name,
            phone = phone
        )
        compositeDisposable.add(
            LoginRetrofitProvider.service
                .clientLogin(loginData).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = { presenter.gotErrorFromAPI(it.localizedMessage ?: "") },
                    onNext = { presenter.gotCodeAndIDFromAPI(it.code, it.id, it.token) })
        )
    }

    override fun saveCredentials(phone: String, name: String, id: Long, token: String) {
        sharedPreferences.edit().putString("phone", phone).putString("token", token).putString("name", name).putLong("id", id)
            .putBoolean("firstInit", true).apply()
    }

    override fun disposeRequests() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}

class LoginCodeInteractorImpl(val presenter: LoginCodePresenter) : LoginInteractor {
    private val compositeDisposable = CompositeDisposable()

    override fun sendLoginRequest(phone: String, name: String) {
        val loginData = LoginData(
            client_name = name,
            phone = phone
        )

        compositeDisposable.add(
            LoginRetrofitProvider.service
                .clientLogin(loginData).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = { presenter.gotErrorFromAPI() },
                    onNext = { presenter.gotCodeFromAPI(it.code, it.token) })
        )
    }

    override fun saveCredentials(phone: String, name: String, id: Long, token: String) {}

    override fun disposeRequests() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}