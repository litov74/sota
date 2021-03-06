package com.development.sota.scooter.ui.map.presentation

import android.Manifest
import android.R.style
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.GravityCompat
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.ActivityMapBinding
import com.development.sota.scooter.ui.doc.LoginUserAgreementActivity
import com.development.sota.scooter.ui.drivings.domain.entities.Order
import com.development.sota.scooter.ui.drivings.domain.entities.OrderStatus
import com.development.sota.scooter.ui.drivings.presentation.DrivingsActivity
import com.development.sota.scooter.ui.drivings.presentation.DrivingsStartTarget
import com.development.sota.scooter.ui.help.presentation.HelpActivity
import com.development.sota.scooter.ui.map.data.Rate
import com.development.sota.scooter.ui.map.data.Scooter
import com.development.sota.scooter.ui.profile.presentation.ProfileActivity
import com.development.sota.scooter.ui.promo.presentation.PromoActivity
import com.development.sota.scooter.ui.purse.presentation.WalletActivity
import com.development.sota.scooter.ui.tutorial.presentation.TutorialActivity
import com.development.sota.scooter.ui.tutorial.presentation.TutorialStartRentActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.*
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.*
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.style.sources.ImageSource
import gone
import kotlinx.android.synthetic.main.item_popup_map.view.*
import kotlinx.android.synthetic.main.item_scooter_driving.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd
import timber.log.Timber
import visible
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import android.view.animation.LinearInterpolator

import android.view.animation.RotateAnimation
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.development.sota.scooter.ui.drivings.presentation.DrivingsListFragmentType


interface MapView : MvpView {
    @AddToEnd
    fun updateScooterMarkers(scootersFeatures: List<Feature>)

    @AddToEnd
    fun initLocationRelationships()

    @AddToEnd
    fun drawRoute(polyline: String)

    @AddToEnd
    fun showScooterCard(scooter: Scooter, status: OrderStatus)

    @AddToEnd
    fun setRateForScooterCard(rate: Rate, scooterId: Long)

    @AddToEnd
    fun setRateForScooterCard(rate: String, scooterId: Long)

    @AddToEnd
    fun setDialogBy(type: MapDialogType)

    @AddToEnd
    fun showToast(text: String)

    @AddToEnd
    fun showBottomActionButtons(visible: Boolean)

    @AddToEnd
    fun showAddOrderError(text: String)

    @AddToEnd
    fun clearParkingZone()

    @AddToEnd
    fun hideScooterCard()

    @AddToEnd
    fun setLoading(by: Boolean)

    @AddToEnd
    fun setActivatingScooter(by: Boolean)

    @AddToEnd
    fun initPopupMapView(orders: List<Order>, bookCount: Int, rentCount: Int)

    @AddToEnd
    fun sendToProfile()

    @AddToEnd
    fun sendToWallet()

    @AddToEnd
    fun sendToDrivingsList()

    @AddToEnd
    fun sendToLookTutorial()

    @AddToEnd
    fun sendToPromo()

    @AddToEnd
    fun sendToDoc()

    @AddToEnd
    fun sendToTutorial()

    @AddToEnd
    fun sendToHelp()

    @AddToEnd
    fun drawGeoZones(feauters: ArrayList<Feature>)

    @AddToEnd
    fun setProfileInfo(clientName: String, balance: String, bonus: String)

    @AddToEnd
    fun showError(msg: String)
}


class MapActivity : MvpAppCompatActivity(), MapView {
    private var _binding: ActivityMapBinding? = null
    private val getBinding get() = _binding!!

    private val presenter by moxyPresenter { MapPresenter(this) }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var map: MapboxMap? = null
    private lateinit var markerManager: MarkerViewManager
    private lateinit var localizationPlugin: LocalizationPlugin
    private lateinit var geoJsonSource: GeoJsonSource
    private val QR_CODE_REQUEST = 555
    private val TUTORIAL_REQUEST = 666
    private lateinit var activity: Activity
    private val disposableJobsBag = hashSetOf<Job>()
//    private lateinit var slideTop: Animation
//    private lateinit var slideBottom: Animation
    private var drawLayer = false

    private var currentShowingScooter = -1L

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent) {
            if (ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(context!!, DrivingsActivity::class.java)
                intent.putExtra("aim", DrivingsStartTarget.QRandCode)

                startActivityForResult(intent, QR_CODE_REQUEST)
            } else {
                getCameraPermission()
            }

        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Mapbox.getInstance(this, getString(R.string.mabox_access_token))

        _binding = ActivityMapBinding.inflate(layoutInflater)

        setContentView(getBinding.root)

        getBinding.contentOfMap.imageButtonMapQr.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(this, DrivingsActivity::class.java)
                intent.putExtra("aim", DrivingsStartTarget.QRandCode)

                startActivityForResult(intent, QR_CODE_REQUEST)
            } else {
                getCameraPermission()
            }
        }

        getBinding.contentOfMap.imageButtonMapPromo.setOnClickListener {
            sendToPromo()
        }

        getBinding.contentOfMap.imageButtonMapMessage.setOnClickListener {
            sendToHelp()
        }
        getBinding.contentOfMap.parkingButton.setOnClickListener {
            presenter.selectShowParking()
        }

        getBinding.contentOfMap.imageButtonMapMenu.setOnClickListener {
            getBinding.drawerLayout.openDrawer(GravityCompat.START)
        }

        getBinding.contentOfMap.imageButtonMapLocation.setOnClickListener {
            Timber.w("Geoposition")
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                initLocationRelationships()
            } else {
                getLocationPermission()
            }
        }


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
            IntentFilter("show_qr")
        );

        getBinding.contentOfMap.mapPopupItem.constraintLayoutParentPopupMap.visibility = View.GONE

        getBinding.contentOfMap.mapScooterItem.constraintLayoutItemScooterParent.visibility = View.GONE

        getBinding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuMapItemProfile -> presenter.sendToProfile()
                R.id.menuMapItemPurse -> presenter.sendToWallet()
                R.id.menuMapItemDrivings -> presenter.sendToTheDrivingsList()
                R.id.menuMapDoc -> presenter.sendToDoc()
                R.id.menuMapItemPromo -> presenter.sendToPromo()
                R.id.menuMapItemLearn -> presenter.sendToLearn()
                R.id.menuMapItemHelp -> presenter.sendToHelp()
            }

            return@setNavigationItemSelectedListener true
        }

        getBinding.contentOfMap.mapPopupItem.cardViewPopupMap.setOnClickListener {
            presenter.sendToTheDrivingsList()
        }

        fusedLocationProviderClient = FusedLocationProviderClient(this)
//        slideTop = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_in_top)
//
//        slideTop.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(p0: Animation?) {
//
//                Log.w("MapActivity", "start slide top animation")
//                getBinding.contentOfMap.mapScooterItem.constraintLayoutItemScooterParent.visibility =
//                        View.VISIBLE
//            }
//
//            override fun onAnimationEnd(p0: Animation?) {
//                Log.w("MapActivity", "end slide top animation")
//            }
//
//            override fun onAnimationRepeat(p0: Animation?) {
//
//            }
//
//        })
//        slideBottom = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_to_bottom)
//        slideBottom.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(p0: Animation?) {
//                Log.w("MapActivity", "start slide bottom animation")
//            }
//
//            override fun onAnimationEnd(p0: Animation?) {
//                getBinding.contentOfMap.mapScooterItem.root.visibility =
//                    View.GONE
//                Log.w("MapActivity", "end slide bottom animation")
//            }
//
//            override fun onAnimationRepeat(p0: Animation?) {
//
//            }
//
//        })

        getBinding.contentOfMap.mapView.getMapAsync { map ->
            this.map = map
            presenter.mapIsReady()
            map.setStyle(Style.MAPBOX_STREETS) { style ->

                style.addImageAsync(
                    SCOOTERS_ICON_THIRD,
                    resources.getDrawable(R.drawable.ic_scooter_on_map_first).toBitmap()
                )
                style.addImageAsync(
                    SCOOTERS_ICON_SECOND,
                    resources.getDrawable(R.drawable.ic_scooter_on_map_second).toBitmap()
                )

                style.addImageAsync(
                    SCOOTERS_ICON_FIRST,
                    resources.getDrawable(R.drawable.ic_scooter_on_map_third).toBitmap()
                )
                style.addImageAsync(
                    SCOOTERS_ICON_CHOSEN,
                    resources.getDrawable(R.drawable.ic_scooter_on_map_selected_third).toBitmap()
                )
                style.addImageAsync(
                    SCOOTERS_ICON_CHOSEN_SECOND,
                    resources.getDrawable(R.drawable.ic_scooter_on_map_selected_second).toBitmap()
                )
                style.addImageAsync(
                    SCOOTERS_ICON_CHOSEN_THIRD,
                    resources.getDrawable(R.drawable.ic_scooter_on_map_selected_third).toBitmap()
                )
                style.addImageAsync(
                    PARKING_ICON_ID,
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.parking
                    )
                )
                style.addImageAsync(
                    MY_MARKER_IMAGE, BitmapFactory.decodeResource(
                        resources,
                        R.drawable.mapbox_marker_icon_default
                    )
                )

                localizationPlugin = LocalizationPlugin(getBinding.contentOfMap.mapView, map, style)
                localizationPlugin.matchMapLanguageWithDeviceDefault()

                val polygon = Polygon.fromLngLats(
                    arrayListOf(
                        arrayListOf(
                            Point.fromLngLat(-180.0, -89.999999999999),
                            Point.fromLngLat(-180.0, 89.99999999999),
                            Point.fromLngLat(179.99999999, 89.99999999999),
                            Point.fromLngLat(179.99999999, -89.99999999999),
                            Point.fromLngLat(0.0, -89.99999999999),
                            Point.fromLngLat(0.0, 89.99999999999)
                        )
                    ) as List<MutableList<Point>>
                )

//                style.addSource(
//                    GeoJsonSource(
//                        GEOZONE_BACKGROUND_SOURCE,
//                        Feature.fromGeometry(polygon),
//                        GeoJsonOptions()
//                    )
//                )
//
//                val layer = FillLayer(
//                    GEOZONE_BACKGROUND_LAYER, GEOZONE_BACKGROUND_SOURCE
//                ).withProperties(
//                    fillColor(GEOZONE_COLOR)
//                )
//
//                style.addLayer(layer)
            }

            markerManager = MarkerViewManager(getBinding.contentOfMap.mapView, map)

            map.addOnMapClickListener {
//                if (getBinding.contentOfMap.mapScooterItem.constraintLayoutItemScooterParent.visibility == View.VISIBLE )
//                    getBinding.contentOfMap.mapScooterItem.constraintLayoutItemScooterParent.startAnimation(slideBottom)

              //  getBinding.contentOfMap.mapScooterItem.constraintLayoutItemScooterParent.visibility = View.GONE

                val pointf: PointF = map.projection.toScreenLocation(it)
                val rectF = RectF(pointf.x - 10, pointf.y - 10, pointf.x + 10, pointf.y + 10)
                val mapClickFeatureList: List<Feature> =
                    map.queryRenderedFeatures(rectF, CIRCLES_LAYER)

                val mapClickScootersList = map.queryRenderedFeatures(rectF, SCOOTERS_LAYER)

                Log.w("MapActivity", "map cluster size "+mapClickFeatureList.size)

                if (mapClickFeatureList.isNotEmpty()) {
                    val clusterLeavesFeatureCollection: FeatureCollection =
                        geoJsonSource.getClusterLeaves(
                            mapClickFeatureList[0],
                            8000, 0
                        )
                    moveCameraToLeavesBounds(clusterLeavesFeatureCollection)
                }

                if (mapClickScootersList.size == 1) {
                    val scooter = mapClickScootersList.first().getProperty("id").asLong

                    if (currentShowingScooter != scooter) {
                        presenter.clickedOnScooterWith(id = scooter)
                    } else {
                        currentShowingScooter = -1L
                    }
                } else {
                    currentShowingScooter = -1L

//                    if (getBinding.contentOfMap.mapScooterItem.constraintLayoutItemScooterParent.visibility == View.VISIBLE) {
//
//                        val animate = TranslateAnimation(
//                            0f,  // fromXDelta
//                            0f,  // toXDelta
//                            0f,  // fromYDelta
//                            getBinding.contentOfMap.mapScooterItem.constraintLayoutItemScooterParent!!.height.toFloat()  // toYDelta
//                        )
//
//                        animate.duration = 500
//                        animate.fillAfter = true
//                        getBinding.contentOfMap.mapScooterItem.constraintLayoutItemScooterParent!!.startAnimation(
//                            animate
//                        )
//                        getBinding.contentOfMap.mapScooterItem.constraintLayoutItemScooterParent!!.gone()
//                    }
                   getBinding.contentOfMap.mapScooterItem.root.visibility = View.GONE

                    presenter.scooterUnselected()

                    map.style?.removeLayer(ROUTE_LAYER)
                    map.style?.removeSource(ROUTE_SOURCE)
                }

                return@addOnMapClickListener true
            }


            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(55.558741, 37.316259), 8.0))
        }

        getLocationPermission()
        getBackgroundLocationPermission()


        presenter.getProfileInfo()
    }



    private fun moveCameraToLeavesBounds(featureCollectionToInspect: FeatureCollection) {
        val latLngList: MutableList<LatLng> = ArrayList()
        if (featureCollectionToInspect.features() != null) {
            for (singleClusterFeature in featureCollectionToInspect.features()!!) {
                val clusterPoint: Point? = singleClusterFeature.geometry() as Point?

                if (clusterPoint != null) {
                    latLngList.add(LatLng(clusterPoint.latitude(), clusterPoint.longitude()))
                }
            }
            if (latLngList.size > 1) {
                val latLngBounds = LatLngBounds.Builder()
                    .includes(latLngList)
                    .build()
                map?.easeCamera(
                    CameraUpdateFactory.newLatLngBounds(latLngBounds, 230),
                    1300
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("MapPresenter"," activity result ${requestCode} ${resultCode} ${data} ")

        if (requestCode == QR_CODE_REQUEST && resultCode == RESULT_OK) {
            presenter.selectScooterByCode(data?.getSerializableExtra("scooter") as Scooter)
        }

        if (requestCode == TUTORIAL_REQUEST && resultCode == RESULT_OK) {
            presenter.selectScooterByCode(data?.getSerializableExtra("scooter") as Scooter)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "TimberArgCount")
    override fun updateScooterMarkers(scootersFeatures: List<Feature>) {
        runOnUiThread {
            map?.style?.removeLayer(CLUSTERS_LAYER)
            map?.style?.removeLayer(SCOOTERS_LAYER)
            map?.style?.removeLayer(CIRCLES_LAYER)
            map?.style?.removeLayer(COUNT_LAYER)
            map?.style?.removeSource(SCOOTERS_SOURCE)

            geoJsonSource = GeoJsonSource(
                SCOOTERS_SOURCE,
                FeatureCollection.fromFeatures(scootersFeatures),
                GeoJsonOptions()
                    .withCluster(true)
                    .withClusterMaxZoom(MAX_CLUSTER_ZOOM_LEVEL)
                    .withClusterRadius(CLUSTER_RADIUS)
            )

            map?.style?.addSource(
                geoJsonSource
            )

            val scootersLayer = SymbolLayer(SCOOTERS_LAYER, SCOOTERS_SOURCE)
            scootersLayer.setProperties(
                iconImage(get(SCOOTER_ICON_SOURCE)),
            )

            map?.style?.addLayer(scootersLayer)

            val layer = intArrayOf(1, ContextCompat.getColor(this, R.color.white))

            val circles = CircleLayer(CLUSTERS_LAYER, SCOOTERS_SOURCE)
            circles.setProperties(
                circleColor(layer[1]),
                circleRadius(14f),

            )
            val pointCount = toNumber(get("point_count"))


            circles.setFilter(
                all(
                    has("point_count"),
                    gte(pointCount, 1)
                )
            )

            map?.style?.addLayer(circles)

            val count = SymbolLayer(COUNT_LAYER, SCOOTERS_SOURCE)
            count.setProperties(
                textField(toString(get("point_count"))),
                textSize(12f),
                textColor(Color.RED),
                textIgnorePlacement(true),
                textAllowOverlap(true)
            )

            map?.style?.removeLayer(COUNT_LAYER)
            map?.style?.addLayer(count)
        }

    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)  {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                ActivityCompat.requestPermissions(
//                    this, arrayOf( Manifest.permission.ACCESS_BACKGROUND_LOCATION),
//                    PERMISSIONS_REQUEST_ACCESS_FINE_BACKGROUND_LOCATION,
//                )
//            }
            presenter.updateLocationPermission(true)
        } else {

            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION,
            )
        }
    }

    private fun getBackgroundLocationPermission() {
//        if (ContextCompat.checkSelfPermission(
//                this.applicationContext,
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            presenter.updateLocationPermission(true)
//        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                ActivityCompat.requestPermissions(
//                   this, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
//                    PERMISSIONS_REQUEST_ACCESS_FINE_BACKGROUND_LOCATION,
//                )
//            }
//        }
    }

    private fun getCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.CAMERA
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            presenter.updateLocationPermission(true)
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                PERMISSIONS_REQUEST_ACCESS_CAMERA
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        presenter.updateLocationPermission(false)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    presenter.updateLocationPermission(true)
                }
            }

            PERMISSIONS_REQUEST_ACCESS_CAMERA -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    val intent = Intent(this, DrivingsActivity::class.java)
                    intent.putExtra("aim", DrivingsStartTarget.QRandCode)

                    startActivityForResult(intent, QR_CODE_REQUEST)


                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onStartEmitted()
    }

    override fun initLocationRelationships() {
        runOnUiThread {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.w("MapActivity", "initLocationRelationships")
                try {
//                    if (ContextCompat.checkSelfPermission(
//                            this.applicationContext,
//                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                        ) == PackageManager.PERMISSION_GRANTED
//                    ) {
//
//                    } else {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                            ActivityCompat.requestPermissions(
//                                this, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
//                                PERMISSIONS_REQUEST_ACCESS_FINE_BACKGROUND_LOCATION,
//                            )
//                        }
//                    }

                    LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
                        Log.w("MapActivity", "last location "+it)

                        if (it != null) {
                            val latLng = LatLng(it.latitude, it.longitude)
                            presenter.position = latLng

                            Log.w("MapActivity", "map is init "+map)



                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0))

                            map?.style?.removeLayer(MY_MARKER_LAYER)
                            map?.style?.removeSource(MY_MARKER_SOURCE)

                            map?.style?.addSource(
                                GeoJsonSource(
                                    MY_MARKER_SOURCE,
                                    presenter.makeFeatureFromLatLng(latLng)
                                )
                            )
                            map?.style?.addLayer(
                                SymbolLayer(MY_MARKER_LAYER, MY_MARKER_SOURCE)
                                    .withProperties(
                                        iconImage(MY_MARKER_IMAGE),
                                        iconAllowOverlap(true),
                                        iconIgnorePlacement(true)
                                    )
                            )


                        }
                    }
                } catch (e: Exception) {
                   e.printStackTrace()
                }
            }
        }
    }

    override fun drawRoute(polyline: String) {
        runOnUiThread {
            map?.style?.removeLayer(ROUTE_LAYER)
            map?.style?.removeSource(ROUTE_SOURCE)

            val source = GeoJsonSource(ROUTE_SOURCE)
            source.setGeoJson(LineString.fromPolyline(polyline, Constants.PRECISION_6))
            map?.style?.addSource(source)

            map?.style?.addLayer(
                LineLayer(ROUTE_LAYER, ROUTE_SOURCE)
                    .withProperties(
                        lineCap(Property.LINE_CAP_ROUND),
                        lineJoin(Property.LINE_JOIN_ROUND),
                        lineWidth(2f),
                        lineColor(Color.parseColor("#09047E"))
                    ),

            )
        }
    }

    override fun showScooterCard(scooter: Scooter, status: OrderStatus) {
        try {
            runOnUiThread {

                Timber.d("current scooter ${currentShowingScooter} scooter show ${scooter.id}")
                if (currentShowingScooter != scooter.id) {

                    getBinding.contentOfMap.mapScooterItem.root.visibility = View.VISIBLE


//                    val animate = TranslateAnimation(
//                        0f,
//                        0f,
//                        getBinding.contentOfMap.mapScooterItem.constraintLayoutItemScooterParent!!.height.toFloat(),  // fromYDelta
//                        0f
//                    )
//
//                    animate.duration = 500
//                    animate.fillAfter = true
//                    getBinding.contentOfMap.mapScooterItem.constraintLayoutItemScooterParent.startAnimation(animate)

                    //getBinding.contentOfMap.mapScooterItem.constraintLayoutItemScooterParent.visibility = View.VISIBLE

//                    if (getBinding.contentOfMap.mapScooterItem.constraintLayoutItemScooterParent.visibility == View.GONE)
//                        getBinding.contentOfMap.mapScooterItem.constraintLayoutItemScooterParent.startAnimation(slideTop)


                    getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.linnearLayoutScooterItemFinishButtons.visibility =
                            View.GONE
                    getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.linnearLayoutScooterItemBookingButtons.visibility =
                            View.GONE
                    getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.linnearLayoutScooterItemRentButtons.visibility =
                            View.GONE


                    getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.constraintLayoutScooterItemPopupRoute.visibility =
                            View.GONE
                    getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.constraintLayoutScooterItemPopupLock.visibility =
                            View.GONE
                    getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.constraintLayoutScooterItemPopupSignal.visibility =
                            View.GONE

                    getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.linnearLayoutScooterItemFirstBookButtons.visibility =
                            View.VISIBLE

                    findViewById<Button>(R.id.buttonItemScooterFirstActivate).setOnClickListener {
                        Log.d("MapActivity", "select activate 3 ")
                        presenter.clickedOnBookButton(scooter.id)
                        currentShowingScooter = -1
                    }
                    Log.d("MapActivity", "status "+status.toString())

                    when (status) {
                        OrderStatus.CANDIDIATE -> {
                            getBinding.contentOfMap.mapScooterItem.textViewItemScooterStateLabel.visibility =
                                    View.GONE
                            getBinding.contentOfMap.mapScooterItem.textViewItemScooterStateValue.visibility =
                                    View.GONE

//                            getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.constraintLayoutScooterItemPopupRouteBackgroundless.visibility =
//                                    View.VISIBLE
//                            getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.constraintLayoutScooterItemPopupRouteBackgroundless.clipToOutline =
//                                    true
//                            getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.constraintLayoutScooterItemPopupRouteBackgroundless.imageView2Backgroundless.clipToOutline =
//                                    true

                            getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.textViewItemScooterMinutePricing.text =
                                    ""

                            val scooterPercentage = scooter.getBatteryPercentage()
                            val scooterInfo = scooter.getScooterRideInfo()

                            val spannable: Spannable =
                                    SpannableString("$scooterPercentage $scooterInfo")

                            spannable.setSpan(
                                    ForegroundColorSpan(Color.BLACK),
                                    0,
                                    scooterPercentage.length,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )

                            spannable.setSpan(
                                    ForegroundColorSpan(Color.GRAY),
                                    scooterPercentage.length,
                                    "$scooterPercentage $scooterInfo".length,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )

                            getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.textViewItemScooterId.text =
                                    "#${scooter.id}"

                            getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.scooterPercent.text = scooterPercentage
                            getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.scooterPercentTime.text = "~$scooterInfo"
                            getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.scooterPercentDistance.text = scooter.getScooterPercentDistance()

                            currentShowingScooter = scooter.id

                        }

                        else -> presenter.sendToTheDrivingsList()
                    }

                }
            }
        } catch (exception: Exception){
            exception.printStackTrace()
        }

    }

    override fun setRateForScooterCard(rate: Rate, scooterId: Long) {
        Log.w("MapActivity", "set rate model")
        runOnUiThread {
            if (currentShowingScooter == scooterId) {

                getBinding.contentOfMap.mapScooterItem.linearLayoutScooterItemInfoTextView.textViewItemScooterMinutePricing.text = rate.minute+"??"
            }
        }
    }

    override fun setRateForScooterCard(rate: String, scooterId: Long) {
        Log.w("MapActivity", "set rate string")
        runOnUiThread {
            if (currentShowingScooter == scooterId) {

                getBinding.contentOfMap.mapScooterItem.linearLayoutScooterItemInfoTextView.textViewItemScooterMinutePricing.text = rate+"??"
            }
        }
    }

    override fun setDialogBy(type: MapDialogType) {
        runOnUiThread {
            when (type) {
                MapDialogType.NO_MONEY_FOR_START ->
                    AlertDialog.Builder(this)
                        .setTitle(getString(R.string.notification))
                        .setMessage(R.string.dialog_balance_data)
                        .setNegativeButton(R.string.dialog_cancel) { dialogInterface: DialogInterface, _ ->
                            presenter.cancelDialog(
                                MapDialogType.NO_MONEY_FOR_START
                            ); dialogInterface.dismiss()
                        }
                        .setPositiveButton(R.string.dialog_add) { dialogInterface: DialogInterface, _ -> presenter.purchaseToBalance(); dialogInterface.dismiss() }
                        .create()
                        .show()

                MapDialogType.BANNED_FOR_BOOKING ->
                    AlertDialog.Builder(this)
                        .setTitle(R.string.dialog_attention)
                        .setMessage(R.string.dialog_block_book)
                        .setNegativeButton(R.string.dialog_ok) { dialogInterface: DialogInterface, _ ->
                            presenter.cancelDialog(
                                MapDialogType.BANNED_FOR_BOOKING
                            ); dialogInterface.dismiss()
                        }
                        .create()
                        .show()
            }
        }
    }

    override fun showToast(text: String) {
        runOnUiThread {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showBottomActionButtons(visible: Boolean) {
        if (visible) {
            getBinding.contentOfMap.mapScooterItem.constraintLayoutItemScooterParent.visibility = View.GONE
            getBinding.contentOfMap.imageButtonMapMessage.visible()
            getBinding.contentOfMap.imageButtonMapLocation.visible()
            getBinding.contentOfMap.imageButtonMapQr.visible()
        } else {
            getBinding.contentOfMap.imageButtonMapMessage.gone()
            getBinding.contentOfMap.imageButtonMapLocation.gone()
            getBinding.contentOfMap.imageButtonMapQr.gone()
        }
    }

    override fun showAddOrderError(text: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.notification))
            .setMessage("???? ?????????? ?? ????????????, ?????? ????????????????????????")
            .setPositiveButton("Ok") { dialogInterface: DialogInterface, _ -> dialogInterface.dismiss() }
            .create()
            .show()
    }

    override fun clearParkingZone() {
        runOnUiThread {
            map?.style?.removeLayer(GEOZONE_LAYER)
            map?.style?.removeLayer(GEOZONE_LAYER_LINE)
            map?.style?.removeLayer(PARKING_IMAGE_LAYER)
            map?.style?.removeLayer(PARKING_LAYER)
            map?.style?.removeLayer(PARKING_BONUS_LAYER)
            map?.style?.removeSource(GEOZONE_SOURCE)
            map?.style?.removeSource(PARKING_SOURCE)
            map?.style?.removeSource(PARKING_BONUS_SOURCE)
        }
    }

    override fun hideScooterCard() {
      getBinding.contentOfMap.mapScooterItem.root.visibility = View.GONE
    }

    override fun setLoading(by: Boolean) {
        runOnUiThread {
            getBinding.contentOfMap.progressBarMap.visibility = if (by) {
                val rotate = RotateAnimation(
                    0f,
                    180f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )
                rotate.duration = 300
                rotate.interpolator = LinearInterpolator()


                getBinding.contentOfMap.progressIndicator.startAnimation(rotate)
                View.VISIBLE

            } else {
                View.GONE
            }

        }
    }

    override fun setActivatingScooter(by: Boolean) {
        if (by) {
            getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.buttonItemScooterFirstActivate.visibility = View.GONE
            getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.progressActivate.visibility = View.VISIBLE
        } else {
            getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.buttonItemScooterFirstActivate.visibility = View.VISIBLE
            getBinding.contentOfMap.mapScooterItem.cardViewScooterItem.progressActivate.visibility = View.GONE
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun initPopupMapView(orders: List<Order>, bookCount: Int, rentCount: Int) {
        runOnUiThread {
            disposableJobsBag.forEach(Job::cancel)
            disposableJobsBag.clear()

            if (bookCount == 0 && rentCount == 0) {
                getBinding.contentOfMap.mapPopupItem.constraintLayoutParentPopupMap.visibility =
                    View.GONE
            } else {
                getBinding.contentOfMap.mapPopupItem.constraintLayoutParentPopupMap.visibility =
                    View.VISIBLE

                if (bookCount > 0 && rentCount == 0 || bookCount == 0 && rentCount > 0) {
                    getBinding.contentOfMap.mapPopupItem.textViewPopupMenuDownBordered.visibility =
                        View.GONE
                    getBinding.contentOfMap.mapPopupItem.textViewPopupMenuDownValue.visibility =
                        View.GONE

                    getBinding.contentOfMap.mapPopupItem.spacerPopupMap1.visibility = View.GONE
                    getBinding.contentOfMap.mapPopupItem.spacerPopupMap2.visibility = View.GONE

                    if (bookCount == 1) {
                        getBinding.contentOfMap.mapPopupItem.textViewPopupMenuUpBordered.text =
                            "${getString(R.string.map_book)} 1"

                        val bookOrder = orders.first { it.status == OrderStatus.BOOKED.value }


                        disposableJobsBag.add(
                            GlobalScope.launch {
                                val orderTime = bookOrder.parseStartTime()

                                while (true) {

                                    if (orderTime != null) {


                                        val dateTime = DateTimeFormatter
                                                .ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                                                .withZone(ZoneOffset.UTC)
                                                .format(Instant.now())



                                        val time = LocalDateTime.now().atZone(ZoneId.systemDefault())
                                                .toInstant().toEpochMilli() - orderTime

                                        val rawMinutes = TimeUnit.MILLISECONDS.toMinutes(time)
                                        val totalSeconds = TimeUnit.MINUTES.toSeconds(rawMinutes)
                                        val tickSeconds = 1
                                        for (second in totalSeconds downTo tickSeconds) {
                                            val time = String.format("%d:%02d:%02d",
                                                    TimeUnit.SECONDS.toHours(second),
                                                    TimeUnit.SECONDS.toMinutes(second),
                                                    second - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(second))
                                            )
                                            getBinding.contentOfMap.mapPopupItem.textViewPopupMenuUpValue.text = time
                                            delay(1000)
                                        }
                                    }
                                }
                                //Server check
                            }
                        )
                    } else if (bookCount > 1) {
                        getBinding.contentOfMap.mapPopupItem.textViewPopupMenuUpBordered.text =
                            "${getString(R.string.map_book)} $bookCount"
                        getBinding.contentOfMap.mapPopupItem.textViewPopupMenuUpValue.text =
                            orders.filter { it.status == OrderStatus.BOOKED.value }.map { it.cost }
                                .sum().toString() + " ??"
                    }

                    if (rentCount >= 1) {
                        getBinding.contentOfMap.mapPopupItem.textViewPopupMenuUpBordered.text =
                            "${getString(R.string.map_rent)} $rentCount"
                        getBinding.contentOfMap.mapPopupItem.textViewPopupMenuUpValue.text =
                            orders.filter { it.status == OrderStatus.ACTIVATED.value }
                                .map { it.cost }
                                .sum().toString() + " ??"
                    }
                } else {
                    getBinding.contentOfMap.mapPopupItem.textViewPopupMenuDownBordered.visibility =
                        View.VISIBLE
                    getBinding.contentOfMap.mapPopupItem.textViewPopupMenuDownValue.visibility =
                        View.VISIBLE

                    getBinding.contentOfMap.mapPopupItem.spacerPopupMap1.visibility = View.VISIBLE
                    getBinding.contentOfMap.mapPopupItem.spacerPopupMap2.visibility = View.VISIBLE

                    if (bookCount == 1) {
                        getBinding.contentOfMap.mapPopupItem.textViewPopupMenuUpBordered.text =
                            "${getString(R.string.map_book)} 1"

                        val bookOrder = orders.first { it.status == OrderStatus.BOOKED.value }

                        disposableJobsBag.add(
                            GlobalScope.launch {
                                val orderTime = bookOrder.parseStartTime()

                                while (true) {

                                    if (orderTime != null) {

                                       val time = LocalDateTime.now().atZone(ZoneId.systemDefault())
                                                .toInstant().toEpochMilli() - orderTime
                                        val rawMinutes = TimeUnit.MILLISECONDS.toMinutes(time)
                                        val totalSeconds = TimeUnit.MINUTES.toSeconds(rawMinutes)
                                        val tickSeconds = 1
                                        for (second in totalSeconds downTo tickSeconds) {
                                            val time = String.format("%d:%02d:%02d",
                                                    TimeUnit.SECONDS.toHours(second),
                                                    TimeUnit.SECONDS.toMinutes(second),
                                                    second - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(second))
                                            )
                                            getBinding.contentOfMap.mapPopupItem.textViewPopupMenuUpValue.text = time
                                            delay(1000)
                                        }
                                    }

                                }
                                //Server check
                            }
                        )
                    } else if (bookCount > 1) {
                        getBinding.contentOfMap.mapPopupItem.textViewPopupMenuUpBordered.text =
                            "${getString(R.string.map_book)} $bookCount"
                        getBinding.contentOfMap.mapPopupItem.textViewPopupMenuUpValue.text =
                            orders.filter { it.status == OrderStatus.BOOKED.value }.map { it.cost }
                                .sum().toString() + " ??"
                    }

                    if (rentCount >= 1) {
                        getBinding.contentOfMap.mapPopupItem.textViewPopupMenuDownBordered.text =
                            "${getString(R.string.map_rent)} $rentCount"
                        getBinding.contentOfMap.mapPopupItem.textViewPopupMenuDownValue.text =
                            orders.filter { it.status == OrderStatus.ACTIVATED.value }
                                .map { it.cost }
                                .sum().toString() + " ??"
                    }
                }
            }
        }
    }

    override fun sendToProfile() {
        runOnUiThread {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun drawGeoZones(feauters: ArrayList<Feature>) {
        runOnUiThread {
            try {
                Timber.d("draw layer ${drawLayer}")


                    map?.style?.removeLayer(GEOZONE_LAYER)
                    map?.style?.removeLayer(GEOZONE_LAYER_LINE)
                    map?.style?.removeLayer(PARKING_LAYER)
                    map?.style?.removeLayer(PARKING_IMAGE_LAYER)
                    map?.style?.removeLayer(PARKING_BONUS_LAYER)

                    map?.style?.removeSource(GEOZONE_SOURCE)
                    map?.style?.removeSource(PARKING_SOURCE)
                    map?.style?.removeSource(PARKING_BONUS_SOURCE)


                    val geozoneJsonSource = GeoJsonSource(
                        GEOZONE_SOURCE,
                        FeatureCollection.fromFeatures(
                            feauters.filter { it.getStringProperty("geoType") == GEOZONE_LABEL }
                        ),
                        GeoJsonOptions()
                    )

                    val parkingJsonSource = GeoJsonSource(
                        PARKING_SOURCE,
                        FeatureCollection.fromFeatures(
                            feauters.filter { it.getStringProperty("geoType") == PARKING_LABEL }
                        ),
                        GeoJsonOptions()
                    )

                    val parkingBonusJsonSource = GeoJsonSource(
                        PARKING_BONUS_SOURCE,
                        FeatureCollection.fromFeatures(
                            feauters.filter { it.getStringProperty("geoType") == PARKING_BONUS_LABEL }
                        ),
                        GeoJsonOptions()
                    )


                    map?.style?.addSource(geozoneJsonSource)
                    map?.style?.addSource(parkingJsonSource)
                    map?.style?.addSource(parkingBonusJsonSource)

                    val geoZoneLayer = FillLayer(GEOZONE_LAYER, GEOZONE_SOURCE)
                    geoZoneLayer.setProperties(
                        fillColor(PARKING_COLOR),
                        fillOpacity(0.3f),
                        fillOutlineColor(PARKING_BONUS_COLOR)
                    )

                    val geoZoneLine = LineLayer(GEOZONE_LAYER_LINE, GEOZONE_SOURCE)
                    geoZoneLine.setProperties(
                        lineColor(PARKING_BONUS_COLOR),
                        lineWidth(1f),
                    )

                    val parkingZoneLayer = LineLayer(PARKING_LAYER, PARKING_SOURCE)
                    parkingZoneLayer.setProperties(
                        lineColor(PARKING_LINE_COLOR),

                        lineWidth(1f),
                    )


                    val parkingLayer = SymbolLayer(PARKING_IMAGE_LAYER, PARKING_SOURCE)
                    parkingLayer.setProperties(
                        iconImage(PARKING_ICON_ID),
                    )

                    val parkingBonusZoneLayer = LineLayer(PARKING_BONUS_LAYER, PARKING_BONUS_SOURCE)
                    parkingZoneLayer.setProperties(
                        lineColor(PARKING_LINE_COLOR),
                        lineWidth(1f),
                    )


                    map?.style?.addLayer(geoZoneLayer)
                    map?.style?.addLayer(parkingLayer)
                    map?.style?.addLayer(geoZoneLine)
                    map?.style?.addLayer(parkingZoneLayer)
                    map?.style?.addLayer(parkingBonusZoneLayer)

            } catch (e: Exception) {
                drawLayer = false
                e.printStackTrace()
            }
        }
    }

    override fun setProfileInfo(clientName: String, balance: String, bonus: String) {
        try {
            val name = getBinding.navView.findViewById<TextView>(R.id.textView6)
            getBinding.navView.findViewById<TextView>(R.id.balanceUser).text = resources.getString(R.string.balance_user, balance)
            getBinding.navView.findViewById<TextView>(R.id.balanceBonusUser).text = resources.getString(R.string.balance_user, bonus)
            name.text = clientName
         } catch (e: Exception) {
             e.printStackTrace()
         }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


    override fun sendToDrivingsList() {
        runOnUiThread {
            val intent = Intent(this, DrivingsActivity::class.java)

            intent.putExtra("aim", DrivingsStartTarget.DrivingList)
            startActivityForResult(intent, QR_CODE_REQUEST)
        }
    }

    override fun sendToLookTutorial() {
        runOnUiThread {
            val intent = Intent(this, TutorialStartRentActivity::class.java)
            startActivityForResult(intent, TUTORIAL_REQUEST)

        }
    }

    override fun sendToPromo() {
        runOnUiThread {
            startActivity(Intent(this, PromoActivity::class.java))
        }
    }

    override fun sendToDoc() {
        runOnUiThread {
            val intent = Intent(this, LoginUserAgreementActivity::class.java)
            startActivity(intent)
        }
    }

    override fun sendToTutorial() {
        runOnUiThread {
            startActivity(Intent(this, TutorialActivity::class.java))
        }
    }

    override fun sendToHelp() {
        runOnUiThread {
            startActivity(Intent(this, HelpActivity::class.java))
        }
    }

    override fun sendToWallet() {
        runOnUiThread {
            startActivity(Intent(this, WalletActivity::class.java))
        }
    }

    private fun bitmapIconFromVector(
        @DrawableRes vectorResId: Int
    ): Bitmap {
        val vectorDrawable = ContextCompat.getDrawable(this, vectorResId)
        val backgroundDrawable =
            ContextCompat.getDrawable(this, R.drawable.ic_purple_circle_with_white_corner)

        vectorDrawable!!.setBounds(
            (((kotlin.math.max(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            ) * 1.35).toInt() - vectorDrawable.intrinsicWidth * 1.125) / 1.125).toInt(),
            (((kotlin.math.max(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            ) * 1.35).toInt() - vectorDrawable.intrinsicHeight * 1.125) / 1.125).toInt(),
            (vectorDrawable.intrinsicWidth * 1.125 + 0.375 * (kotlin.math.max(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            ) * 1.35 - vectorDrawable.intrinsicWidth * 1.125) / 1.125).toInt(),
            (vectorDrawable.intrinsicHeight * 1.125 + 0.375 * (kotlin.math.max(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            ) * 1.35 - vectorDrawable.intrinsicHeight * 1.125) / 1.125).toInt()
        )
        backgroundDrawable!!.setBounds(
            0,
            0,
            (kotlin.math.max(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            ) * 1.35).toInt(),
            (kotlin.math.max(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            ) * 1.35).toInt()
        )

        val bitmap = Bitmap.createBitmap(
            (kotlin.math.max(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            ) * 1.35).toInt(),
            (kotlin.math.max(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            ) * 1.35).toInt(),
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        backgroundDrawable.draw(canvas)
        vectorDrawable.draw(canvas)

        return Bitmap.createBitmap(bitmap)
    }

    private fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
        var drawable = ContextCompat.getDrawable(this, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onBackPressed() {}

    companion object {
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 99
        const val PERMISSIONS_REQUEST_ACCESS_FINE_BACKGROUND_LOCATION = 111
        const val PERMISSIONS_REQUEST_ACCESS_CAMERA = 100
        const val MAX_CLUSTER_ZOOM_LEVEL = 14
        const val CLUSTER_RADIUS = 50
        const val MY_MARKER_SOURCE = "my-marker-source"
        const val MY_MARKER_LAYER = "my-marker-layer"
        const val MY_MARKER_IMAGE = "my-marker-image"
        const val SCOOTER_ICON_SOURCE = "scooter-image"
        const val CIRCLES_LAYER = "circles"
        const val CLUSTERS_LAYER = "clusters"
        const val SCOOTERS_SOURCE = "scooters"
        const val SCOOTERS_LAYER = "scooters-layer"
        const val SCOOTERS_ICON_THIRD = "scooter-third"
        const val SCOOTERS_ICON_SECOND = "scooter-second"
        const val SCOOTERS_ICON_FIRST = "scooter-first"
        const val SCOOTERS_ICON_CHOSEN = "scooter-chosen"
        const val SCOOTERS_ICON_CHOSEN_FIRST = "scooter-chosen-first"
        const val SCOOTERS_ICON_CHOSEN_SECOND = "scooter-chosen-second"
        const val SCOOTERS_ICON_CHOSEN_THIRD = "scooter-chosen-third"
        const val COUNT_LAYER = "count"
        const val ROUTE_LAYER = "route"
        const val ROUTE_SOURCE = "route-source"
        const val GEOZONE_BACKGROUND_LAYER = "geozone-background"
        const val GEOZONE_BACKGROUND_SOURCE = "geozone-background-source"
        const val GEOZONE_LAYER = "geozone"
        const val GEOZONE_LAYER_LINE = "geozone-line"
        const val GEOZONE_SOURCE = "geozone-source"
        const val PARKING_LAYER = "parking"
        const val PARKING_IMAGE_LAYER = "parking-image"
        const val PARKING_SOURCE = "parking-source"
        const val PARKING_BONUS_LAYER = "parking-bonus"
        const val PARKING_BONUS_SOURCE = "parking-bonus-source"
        const val PARKING_IMAGE = "parking-image"
        const val PARKING_IMAGE_FIRST = "parking-image-first"
        const val PARKING_IMAGE_SECOND = "parking-image-second"
        const val PARKING_IMAGE_THIRD = "parking-image-third"
        const val PARKING_ICON_ID = "parking-icon"

        val GEOZONE_COLOR = Color.parseColor("#40FF453A")
        val GEOZONE_LINE_COLOR = Color.parseColor("#FF453A")
        val PARKING_LINE_COLOR = Color.parseColor("#2F80ED")
        val PARKING_BONUS_COLOR = Color.parseColor("#14D53D")
        val PARKING_COLOR = Color.parseColor("#b0f8bf")

        val GEOZONE_LABEL = "Allowed"
        val PARKING_LABEL = "Parking"
        val PARKING_BONUS_LABEL = "BParking"

        val TRANSPARENT_COLOR = Color.parseColor("#40FFFFFF")

        val MAX_BOOK_TIME = 900000L
    }
}

enum class MapDialogType {
    NO_MONEY_FOR_START, BANNED_FOR_BOOKING
}