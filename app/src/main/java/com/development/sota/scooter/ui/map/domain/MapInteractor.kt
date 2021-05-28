package com.development.sota.scooter.ui.map.domain

import com.development.sota.scooter.R
import com.development.sota.scooter.common.base.BaseInteractor
import com.development.sota.scooter.db.SharedPreferencesProvider
import com.development.sota.scooter.net.ClientRetrofitProvider
import com.development.sota.scooter.net.MapRetrofitProvider
import com.development.sota.scooter.net.OrdersRetrofitProvider
import com.development.sota.scooter.ui.map.data.ClientUpdateToken
import com.development.sota.scooter.ui.map.data.Scooter
import com.development.sota.scooter.ui.map.presentation.MapPresenter
import com.development.sota.scooter.ui.profile.domain.entities.ClientUpdateNameData
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


interface MapInteractor : BaseInteractor {
    fun getAllScooters()
    fun getRouteFor(destination: LatLng, origin: LatLng)
    fun getRate(scooterId: Long)
    fun checkNoneNullBalanceAndBookBan(scooterId: Long, withActivation: Boolean = false)
    fun addOrder(startTime: String, scooterId: Long, withActivation: Boolean = false)
    fun getOrders()
    fun activateOrder(id: Long)
    fun getScootersAndOrders()
    fun getGeoZoneForRate(scooterZoneId: Int, scootedId:Long)
    fun getGeoZone()
    fun getCodeOfScooterAndNull(): Long?
    fun getProfileInfo()
    fun sendFirebaseToken(token: String)
}

class MapInteractorImpl(private val presenter: MapPresenter) : MapInteractor {
    private val compositeDisposable = CompositeDisposable()
    private val sharedPreferences = SharedPreferencesProvider(presenter.context).sharedPreferences


    override fun getProfileInfo() {
        compositeDisposable.add(
            ClientRetrofitProvider.service
                .getClient(sharedPreferences.getLong("id", -1).toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    // TODO:
                    //onError = { presenter.showError(it.localizedMessage ?: "") },
                    onNext = {
                        presenter.gotClientsFromAPI(it)
                    }

                )
        )
    }

    override fun sendFirebaseToken(token: String) {
        compositeDisposable.add(
            ClientRetrofitProvider.service
                .updateClientFBToken(ClientUpdateToken(sharedPreferences.getLong("id", -1), token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {

                    },
                    onError = {
                        it.printStackTrace()
                         }
                )
        )
    }

    override fun getAllScooters() {
        compositeDisposable.add(
            MapRetrofitProvider.service.getScooters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { presenter.scootersGotFromServer(it as ArrayList<Scooter>) },
                    onError = { presenter.errorGotFromServer(it.localizedMessage) })
        )
    }

    override fun getRate(scooterId: Long) {
        compositeDisposable.add(
            MapRetrofitProvider.service.getRate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { presenter.ratesGotFromServer(it, scooterId) },
                    onError = { presenter.errorGotFromServer(it.localizedMessage) }
                )
        )
    }

    override fun checkNoneNullBalanceAndBookBan(scooterId: Long, withActivation: Boolean) {
        compositeDisposable.add(
            Observables.zip(
                ClientRetrofitProvider.service.getClient(
                    sharedPreferences.getLong("id", -1).toString()
                ),
                ClientRetrofitProvider.service.checkBookingBlock(
                    sharedPreferences.getLong("id", -1).toString()
                )
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        presenter.getInfoAboutUserFromServer(
                            Pair(it.first[0], it.second),
                            scooterId = scooterId,
                            withActivation = withActivation
                        )
                    },
                    onError = { presenter.errorGotFromServer(it.localizedMessage) }
                )
        )
    }

    override fun getRouteFor(destination: LatLng, origin: LatLng) {
        val client = MapboxDirections.builder()
            .accessToken(presenter.context.getString(R.string.mabox_access_token))
            .destination(Point.fromLngLat(destination.longitude, destination.latitude))
            .origin(Point.fromLngLat(origin.longitude, origin.latitude))
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .build()

        client.enqueueCall(object : Callback<DirectionsResponse> {
            override fun onResponse(
                call: Call<DirectionsResponse>,
                response: Response<DirectionsResponse>
            ) {
                val routes = response.body()?.routes()

                if (routes != null && routes.isNotEmpty()) {
                    presenter.gotRouteFromServer(routes[0])
                }
            }

            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                presenter.errorGotFromServer(t.localizedMessage)
            }

        })
    }

    override fun addOrder(startTime: String, scooterId: Long, withActivation: Boolean) {
        compositeDisposable.add(
            OrdersRetrofitProvider.service.addOrder(
                startTime = startTime,
                scooterId = scooterId,
                clientId = sharedPreferences.getLong("id", -1)
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { presenter.newOrderGotFromServer(it.id, scooterId, withActivation) },
                    onError = { presenter.addOrderError(it.localizedMessage) }
                )
        )
    }

    override fun getOrders() {
        compositeDisposable.add(
            OrdersRetrofitProvider.service.getOrders(sharedPreferences.getLong("id", -1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { presenter.ordersGotFromServer(it) },
                    onError = { presenter.errorGotFromServer(it.localizedMessage) }
                )
        )
    }

    override fun activateOrder(id: Long) {
        compositeDisposable.add(
            OrdersRetrofitProvider.service.activateOrder(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {  presenter.activateSucc() },
                        onError = { presenter.errorGotFromServer(it.localizedMessage) }
                )
        )

    }

    override fun getScootersAndOrders() {
        compositeDisposable.add(
            Observables.zip(
                MapRetrofitProvider.service.getScooters(),
                OrdersRetrofitProvider.service.getOrders(sharedPreferences.getLong("id", -1L))
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { presenter.scootersAndOrdersGotFormServer(it) },
                    onError = { presenter.errorGotFromServer(it.localizedMessage) }
                )
        )
    }

    override fun getGeoZoneForRate(scooterZoneId: Int, scootedId:Long) {
        compositeDisposable.add(
            MapRetrofitProvider.jsonlessService.getGeoZone()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { presenter.geoZoneForPrice(it, scooterZoneId, scootedId) },
                    onError = { presenter.errorGotFromServer(it.localizedMessage ?: "") }
                )
        )
    }

    override fun getGeoZone() {
        compositeDisposable.add(
            MapRetrofitProvider.jsonlessService.getGeoZone()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { presenter.geoZoneGot(it) },
                    onError = { presenter.errorGotFromServer(it.localizedMessage ?: "") }
                )
        )
    }

    override fun getCodeOfScooterAndNull(): Long? {
        return if (sharedPreferences.contains("scooter_show")) {
            val id = sharedPreferences.getLong("scooter_id", -1)
            sharedPreferences.edit().remove("scooter_id").apply()
            id
        } else {
            null
        }
    }

    override fun disposeRequests() {
        compositeDisposable.clear()
    }
}