package com.development.sota.scooter.ui.map.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.development.sota.scooter.R
import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.drivings.domain.entities.Order
import com.development.sota.scooter.ui.drivings.domain.entities.OrderStatus
import com.development.sota.scooter.ui.map.data.*
import com.development.sota.scooter.ui.map.domain.MapInteractor
import com.development.sota.scooter.ui.map.domain.MapInteractorImpl
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Feature
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import timber.log.Timber
import java.util.*


class  MapPresenter(val context: Context) : MvpPresenter<MapView>(), BasePresenter {
    private val interactor: MapInteractor = MapInteractorImpl(this)

    private var scooters = arrayListOf<Scooter>()

    var locationPermissionGranted = false
    var parkingIsVisible = false
    var features = arrayListOf<Feature>()
    var firstAttachGeo = true
    var position: LatLng = LatLng(55.558741, 37.316259)
        set(value) {
            if (currentScooter != null) {
                interactor.getRouteFor(destination = value, origin = currentScooter!!.getLatLng())
            }
            field = value
        }

    val isNeedFirstScroll = false
    var rates = arrayListOf<Rate>() //Minute, Hour
    var scootersWithOrders = hashMapOf<Long, Long>() //Minute, Hour
    lateinit var scootersGeoJsonSource: List<Feature>
    var usingScooters = hashSetOf<Long>()

    var currentScooter: Scooter? = null
        set(value) {
            if (value != currentScooter) {
                makeFeaturesFromScootersAndSendToMap()
            }
            field = value
        }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                try {
                    interactor.getScootersAndOrders()
                    interactor.getGeoZone()
                } catch (e: Exception) {
                    Log.w("Error calling server", e.localizedMessage)
                }

                delay(30000)
            }
        }

    }

    fun scootersGotFromServer(scooters: ArrayList<Scooter>) {
        this.scooters = scooters

        makeFeaturesFromScootersAndSendToMap()
    }

    fun ordersGotFromServer(orders: List<Order>) {
        viewState.setLoading(false)

        for (order in orders) {
            if (order.status == OrderStatus.CLOSED.value) {
                scootersWithOrders =
                    scootersWithOrders.filterValues { it != order.id } as HashMap<Long, Long>
            }
        }

        initMapPopupView(orders)
    }

    fun scootersAndOrdersGotFormServer(scootersAndOrders: Pair<List<Scooter>, List<Order>>) {
        GlobalScope.launch {
                usingScooters.addAll(scootersAndOrders.second.map { it.scooter })
                scooters = scootersAndOrders.first.filter { it.status == ScooterStatus.ONLINE.value || it.id in usingScooters } as ArrayList<Scooter>

                viewState.setLoading(false)

                makeFeaturesFromScootersAndSendToMap()
                initMapPopupView(scootersAndOrders.second)

        }
    }

    fun initMapPopupView(orders: List<Order>) {
        val bookCount = orders.count { it.status == OrderStatus.BOOKED.value }
        val rentCount = orders.count { it.status == OrderStatus.ACTIVATED.value }

        if (rentCount != 0)
            viewState.initPopupMapView(orders, bookCount, rentCount)
    }

    fun ratesGotFromServer(rates: List<Rate>, scooterId: Long) {
        this.rates = rates as ArrayList<Rate>

        val neededRate = scooters.first { scooter: Scooter -> scooter.id == scooterId }.rate
        viewState.setRateForScooterCard(
            rates.first { rate: Rate -> rate.id == neededRate.toLong() },
            scooterId
        )
    }

    fun getScooters(): ArrayList<Scooter> {
        return scooters
    }

    fun clickedOnScooterWith(id: Long) {
        currentScooter = scooters.firstOrNull { it.id == id }

        if (currentScooter != null) {
            viewState.showBottomActionButtons(false)
            viewState.showScooterCard(currentScooter!!, OrderStatus.CANDIDIATE)

            interactor.getRouteFor(destination = position, origin = currentScooter!!.getLatLng())
            interactor.getGeoZoneForRate(currentScooter!!.geozone, id)
        }
    }

    fun selectScooterByCode(scooter:Scooter) {

        Log.d("MapPresenter"," scooter by code ${scooter} ")
        if (scooter != null) {
            currentScooter = scooter
            viewState.showBottomActionButtons(false)
            viewState.showScooterCard(scooter!!, OrderStatus.CANDIDIATE)
            interactor.getRouteFor(destination = position, origin = scooter!!.getLatLng())
            interactor.getGeoZoneForRate(currentScooter!!.geozone, currentScooter!!.id)
        } else {
            viewState.hideScooterCard()
            viewState.showBottomActionButtons(true)
        }
    }

    fun selectShowParking() {
        if (parkingIsVisible) {
            viewState.clearParkingZone()
        } else {
            viewState.drawGeoZones(features)
        }
        parkingIsVisible = !parkingIsVisible
    }

    @SuppressLint("TimberArgCount")
    fun errorGotFromServer(error: String) {
        Log.w("Error calling server", error)
      //  viewState.showToast(context.getString(R.string.error_api))
        viewState.setActivatingScooter(false)
        viewState.setLoading(false)
    }

    @SuppressLint("TimberArgCount")
    fun addOrderError(error: String) {
        viewState.setActivatingScooter(false)
        viewState.showAddOrderError(context.getString(R.string.error_api))
        viewState.setLoading(false)
    }

    fun newOrderGotFromServer(orderId: Long, scooterId: Long, withActivation: Boolean) {
        scootersWithOrders[scooterId] = orderId
        interactor.activateOrder(orderId)
    }

    fun activateSucc() {
        currentScooter = null
        viewState.setActivatingScooter(false)
        viewState.hideScooterCard()
        viewState.sendToLookTutorial()
       // viewState.sendToDrivingsList()
    }

    fun updateLocationPermission(permission: Boolean) {
        Log.w("MapPresenter", "permission location is grant: "+permission)
        locationPermissionGranted = permission

        if (locationPermissionGranted) viewState.initLocationRelationships()
    }

    fun gotRouteFromServer(route: DirectionsRoute) {
        viewState.drawRoute(route.geometry() ?: "{}")
    }

    fun getInfoAboutUserFromServer(
        info: Pair<Client, BookingBlockResponse>,
        scooterId: Long,
        withActivation: Boolean
    ) {
        try {
            if (info.first.balance.toDouble() > 0 && !info.second.blocked) {
                Log.w("MapOrder", "addOrder ")
                interactor.addOrder(
                    startTime = Order.dateFormatter.format(Date()),
                    scooterId = scooterId,
                    withActivation = false
                )
            } else if (info.first.balance.toDouble() <= 0) {

                viewState.setActivatingScooter(false)
                viewState.setDialogBy(MapDialogType.NO_MONEY_FOR_START)

            } else if (info.second.blocked) {
                viewState.setActivatingScooter(false)
                viewState.setDialogBy(MapDialogType.BANNED_FOR_BOOKING)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelDialog(type: MapDialogType) {

    }

    fun clickedOnBookButton(scooterId: Long) {
        Log.d("MapPresenter", "click id "+scooterId)
        viewState.setActivatingScooter(true)
        interactor.checkNoneNullBalanceAndBookBan(scooterId)
    }


    fun purchaseToBalance() {
        viewState.sendToWallet()
    }

    fun sendToTheDrivingsList() {
        viewState.sendToDrivingsList()
    }

    fun sendCurrentFBToken() {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {

                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                interactor.sendFirebaseToken(token)

            })
        } catch (e: Exception) {

        }
    }

    fun onStartEmitted() {
        sendCurrentFBToken()
        interactor.getScootersAndOrders()
        getProfileInfo()
        val id = interactor.getCodeOfScooterAndNull()

        if (id != null) {
            clickedOnScooterWith(id)
        }

    }

    fun mapIsReady() {
        viewState.initLocationRelationships()
        interactor.getGeoZone()
    }

    fun scooterUnselected() {
        currentScooter = null
        viewState.showBottomActionButtons(true)
    }

    fun geoZoneGot(geoZoneJson: String) {


            val type = object : TypeToken<List<JsonObject>>() {}.type
            val jsonArray = Gson().fromJson<List<JsonObject>>(
                geoZoneJson.replace(" ", "").replace("[[", "[[[").replace("]]", "]]]"), type
            )


            for (i in jsonArray) {
                features.add(Feature.fromJson(i.toString()))
            }

            viewState.drawGeoZones(features)
            firstAttachGeo = false

    }


    fun geoZoneForPrice(geoZoneJson: String, scooterZoneId: Int, scooterId: Long) {
        val type = object : TypeToken<List<JsonObject>>() {}.type
        val jsonArray = Gson().fromJson<List<JsonObject>>(
            geoZoneJson.replace(" ", "").replace("[[", "[[[").replace("]]", "]]]"), type
        )
        val features = arrayListOf<Feature>()

        for (i in jsonArray) {

           if (i.get("properties").asJsonObject.get("id").asInt == scooterZoneId) {

               viewState.setRateForScooterCard(
                   i.get("properties").asJsonObject.get("minute_rate").asString,
                   scooterId
               )
           }

        }

        //viewState.drawGeoZones(features)
    }

    private fun makeFeaturesFromScootersAndSendToMap() {
        GlobalScope.launch {
            scootersGeoJsonSource = scooters.groupBy { it.getScooterIcon() }.mapValues { entry ->
                entry.value.map {
                    Feature.fromJson(
                        """{
                                "type": "Feature",
                                "geometry": {
                                    "type": "Point",
                                    "coordinates": [${it.longitude}, ${it.latitude}]
                                },
                                "properties": {
                                    "id": ${it.id},
                                    "scooter-image": ${
                            if (currentScooter != null && currentScooter!!.id == it.id) {
                                "scooter-chosen"
                            } else {
                                when (it.getScooterIcon()) {
                                    R.drawable.ic_icon_scooter_third -> "scooter-third"
                                    R.drawable.ic_icon_scooter_second -> "scooter-second"
                                    R.drawable.ic_icon_scooter_first -> "scooter-first"
                                    else -> "scooter-third"
                                }
                            }
                        }
                                }
                            }"""
                    )
                }
            }.values.flatten()

            // TODO: было исключение "java.lang.ArrayIndexOutOfBoundsException: length=49; index=49", нужно разобраться как так произошло
            try {
                viewState.updateScooterMarkers(scootersGeoJsonSource)
            } catch (e: Exception) {
            }
        }
    }

    fun makeFeatureFromLatLng(latLng: LatLng): Feature {
        return Feature.fromJson(
            """{
                                "type": "Feature",
                                "geometry": {
                                    "type": "Point",
                                    "coordinates": [${latLng.longitude}, ${latLng.latitude}]
                                }
                            }"""
        )
    }

    override fun onDestroyCalled() {
        interactor.disposeRequests()
    }

    fun sendToWallet() {
        viewState.sendToWallet()
    }

    fun sendToProfile() {
        viewState.sendToProfile()


    }

    override fun onDestroy() {
        super.onDestroy()
        interactor.disposeRequests()
    }



    fun sendToHelp() {
        viewState.sendToHelp()
    }

    fun sendToLearn() {
        viewState.sendToTutorial()
    }

    fun sendToPromo() {
        viewState.sendToPromo()
    }

    fun sendToDoc() {
        viewState.sendToDoc()
    }

    fun getProfileInfo() {
        viewState.setLoading(true)
        interactor.getProfileInfo()
    }

    fun showError(msg: String) {
        viewState.setLoading(false)
        viewState.showError(msg)
    }

    fun gotClientsFromAPI(arrayList: List<Client>) {
        viewState.setLoading(false)
        val item = arrayList[0]
        val clientName = item.clientName
        val balance = item.balance
        val bonus = item.bonus_balance
        viewState.setProfileInfo(clientName,balance, bonus)
    }}