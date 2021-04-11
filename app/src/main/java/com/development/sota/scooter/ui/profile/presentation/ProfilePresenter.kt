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

    fun setProfileInfo(clientName: String, phone: String) {
        viewState.setLoading(true)
        interactor.setProfileInfo(clientName, phone)
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

    fun completeUpdateProfile() {
        viewState.setLoading(false)
        viewState.profileUpdate()
    }
}