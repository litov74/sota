package com.development.sota.scooter.ui.drivings.presentation.fragments.list

import android.content.Context
import com.development.sota.scooter.R
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.drivings.domain.DrivingsListInteractor
import com.development.sota.scooter.ui.drivings.domain.DrivingsListInteractorImpl
import com.development.sota.scooter.ui.drivings.domain.entities.Order
import com.development.sota.scooter.ui.drivings.domain.entities.OrderStatus
import com.development.sota.scooter.ui.drivings.domain.entities.OrderWithStatus
import com.development.sota.scooter.ui.map.data.RateType
import com.development.sota.scooter.ui.map.data.Scooter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moxy.MvpPresenter

class DrivingsListPresenter(val context: Context) : MvpPresenter<DrivingsListView>(),
    BasePresenter {
    private val interactor: DrivingsListInteractor = DrivingsListInteractorImpl(this)
    private var orders = arrayListOf<Order>()
    private var selectedScooterId: Long = 0
    private var ordersWithStatuses = arrayListOf<OrderWithStatus>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.setLoading(true)
        interactor.getAllOrdersAndScooters()
        updateScooterInfo()
    }

    private fun updateScooterInfo() {
        CoroutineScope(IO).launch {
            delay(60000)
            CoroutineScope(Main).launch {
                viewState.setLoading(true)
                interactor.getAllOrdersAndScooters()
                updateScooterInfo()
            }
        }
    }

    fun gotOrdersAndScootersFromServer(ordersAndScooters: Pair<List<Order>, List<Scooter>>) {
        this.orders = ordersAndScooters.first as ArrayList<Order>

        ordersWithStatuses.clear()
        val finishedOrders = arrayListOf<OrderWithStatus>()

        for (order in orders) {
            when (order.status) {
                OrderStatus.CLOSED.value ->
                    finishedOrders.add(
                        OrderWithStatus(
                            order,
                            ordersAndScooters.second.first { it.id == order.scooter },
                            OrderStatus.CLOSED
                        )
                    )
                OrderStatus.CANCELED.value ->
                    finishedOrders.add(
                        OrderWithStatus(
                            order,
                            ordersAndScooters.second.first { it.id == order.scooter },
                            OrderStatus.CANCELED
                        )
                    )
                OrderStatus.BOOKED.value ->
                    ordersWithStatuses.add(
                        OrderWithStatus(
                            order,
                            ordersAndScooters.second.first { it.id == order.scooter },
                            OrderStatus.BOOKED
                        )
                    )
                OrderStatus.ACTIVATED.value ->
                    ordersWithStatuses.add(
                        OrderWithStatus(
                            order,
                            ordersAndScooters.second.first { it.id == order.scooter },
                            OrderStatus.ACTIVATED
                        )
                    )
                else ->
                    ordersWithStatuses.add(
                        OrderWithStatus(
                            order,
                            ordersAndScooters.second.first { it.id == order.scooter },
                            OrderStatus.CLOSED
                        )
                    )
            }
        }

        viewState.setLoading(false)
        viewState.initViewPager2(Pair(ordersWithStatuses, finishedOrders))
    }

    fun gotErrorFromServer(message: String) {
        viewState.setLoading(false)
        viewState.showToast(message)
    }

    fun cancelOrder(id: Long) {
        viewState.setLoading(true)

        interactor.cancelOrder(id)
    }

    fun resumeView() {

    }

    fun activateOrder(id: Long) {
        selectedScooterId = id;
        viewState.setLoading(true)

        interactor.activateOrder(id)
    }

    fun resumeOrder(id: Long) {
        selectedScooterId = id;
        viewState.setLoading(true)

        interactor.resumeOrder(id)
    }

    fun pauseOrder(id: Long) {
        selectedScooterId = id;
        viewState.setLoading(true)
        interactor.pauseOrder(id)
    }

    fun setRateAndActivate(id: Long, type: RateType) {
        viewState.setLoading(true)

        interactor.setRateAndActivateOrder(id, type)
    }

    fun closeSucc() {
        viewState.openFinishTutorial()
        interactor.getAllOrdersAndScooters()
    }

    fun closeError() {

        viewState.setLoading(false)
        viewState.showToast("Самокат не в зоне завершения аренды")
    }

    fun closeOrder(id: Long) {
        viewState.setLoading(true)

        interactor.closeOrder(id)
    }

    fun showError(error: String) {
        viewState.showToast(error)
    }


    fun pauseScooterSuccess() {
        interactor.getAllOrdersAndScooters()
        viewState.pauseScooterSuccess()

    }

    fun pauseScooterError() {
        viewState.setLoading(false)
        viewState.pauseScooterError()

    }

    fun repeatScooterPause() {
        viewState.setLoading(true)
        interactor.pauseOrder(selectedScooterId)
    }

    fun actionEnded(success: Boolean, actionToPerform: () -> Unit = {}) {
        if (success) {
            actionToPerform()
        } else {
            viewState.showToast(context.getString(R.string.error_api))
        }
    }

    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }

}