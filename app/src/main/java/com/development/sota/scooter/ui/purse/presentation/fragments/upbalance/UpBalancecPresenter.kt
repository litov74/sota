package com.development.sota.scooter.ui.purse.presentation.fragments.upbalance

import android.content.Context
import android.util.Log
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.purse.domain.WalletUpBalanceInteractor
import com.development.sota.scooter.ui.purse.domain.WalletUpBalanceInteractorImpl
import com.development.sota.scooter.ui.purse.domain.entities.UpBalancePackageModel
import moxy.MvpPresenter

class UpBalancePresenter(val context: Context) : MvpPresenter<UpBalanceView>(), BasePresenter {

    private val interactor: WalletUpBalanceInteractor = WalletUpBalanceInteractorImpl(this)
    private lateinit var selectedModel: UpBalancePackageModel
    private var currentList: List<UpBalancePackageModel> = ArrayList()

    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }

    fun resumeView() {
        getUpBalancePackages()
    }

    fun getUpBalancePackages() {
        interactor.getReplenishmentPackages();
    }

    fun showProgress(boolean: Boolean) {
        viewState.showProgress(boolean)
    }

    fun upBalancePackage(model: UpBalancePackageModel) {
        selectedModel = model
        viewState.showAlertPayment(selectedModel.cost.toString())

    }

    fun confirmUpBalance() {
        interactor.upBalance(selectedModel)
    }

    fun balanceUpdated(updatedModel: UpBalancePackageModel) {
        viewState.showUpBalanceOk(updatedModel.income.toString())
        viewState.updateUserBalance()
        getUpBalancePackages()
    }

    fun showMessage(message:String) {
        viewState.updateUserBalance()
        viewState.showProgress(false)
        viewState.showToast(message)
    }

    fun setUpBalancePackages(list: List<UpBalancePackageModel>) {
        currentList = list
        viewState.showProgress(false)
        viewState.setListPackages(currentList)
    }

}