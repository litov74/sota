package com.development.sota.scooter.ui.profile.presentation

import android.content.Context
import com.development.sota.scooter.ui.map.data.Client
import com.development.sota.scooter.ui.profile.domain.ProfileInteractor
import com.development.sota.scooter.ui.profile.domain.ProfileInteractorImpl
import moxy.MvpPresenter

class ProfilePresenter(val context: Context) : MvpPresenter<ProfileView>() {

    private val interactor: ProfileInteractor = ProfileInteractorImpl(this)


    fun getProfileInfo() {
        viewState.setLoading(true)
        interactor.getProfileInfo()
    }

    fun showLoading(boolean: Boolean) {
        viewState.setLoading(boolean)
    }

    fun showError(msg: String) {
        viewState.setLoading(false)
        viewState.showError(msg)
    }

    fun gotClientsFromAPI(arrayList: List<Client>) {
        viewState.setLoading(false)
        val item = arrayList[0]
        val clientName = item.clientName
        val phone = item.phone
        val surname = item.surname
        val clientPhoto = item.clientPhoto
        viewState.setProfileInfo(clientName, surname, phone, clientPhoto)
    }

    fun progressUpdateClientName(boolean: Boolean) {
        viewState.setProgressUpdateName(boolean)
    }

    fun updateProfileInfo(clientName: String, phone: String) {
        interactor.setProfileInfo(clientName, phone)
    }

    fun completeUpdateProfile() {
        viewState.setLoading(false)
        viewState.profileUpdate()
    }
}