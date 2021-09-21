package com.development.sota.scooter.ui.drivings.presentation

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.development.sota.scooter.MainActivity
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.ActivityDrivingsBinding
import com.development.sota.scooter.ui.drivings.presentation.fragments.QRFragment
import com.development.sota.scooter.ui.drivings.presentation.fragments.code.DrivingsCodeFragment
import com.development.sota.scooter.ui.drivings.presentation.fragments.list.DrivingsListFragment
import com.development.sota.scooter.ui.map.data.Scooter
import com.development.sota.scooter.ui.map.presentation.MapActivity
import moxy.MvpAppCompatActivity
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd
import java.io.Serializable


interface DrivingsView : MvpView {
    @AddToEnd
    fun showToast(string: String)

    @AddToEnd
    fun setFragmentByType(type: DrivingsListFragmentType)

    @AddToEnd
    fun setResultCodeScooter(scooter: Scooter)
}

interface DrivingsActivityView {
    fun gotCode(code: Long, delegate: DrivingsFragmentView)

    fun onBackPressedByType(type: DrivingsListFragmentType)

    fun sendToCodeActivity()

    fun toggleLantern()
}

interface DrivingsFragmentView {
    fun gotResultOfCodeChecking(result: Boolean)
}

class DrivingsActivity : MvpAppCompatActivity(), DrivingsView, DrivingsActivityView {
    private val presenter by moxyPresenter { DrivingsPresenter(this) }

    private var _binding: ActivityDrivingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var activity: Activity
    private var saveQrFragment: MvpAppCompatFragment? = null
    private var saveCodeFragment: MvpAppCompatFragment? = null
    private var saveListFragment: MvpAppCompatFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDrivingsBinding.inflate(layoutInflater)
        this.activity = this
        if (intent.getSerializableExtra("aim") == DrivingsStartTarget.QRandCode) {
            presenter.updateFragmentType(DrivingsListFragmentType.QR)
        } else {
            presenter.updateFragmentType(DrivingsListFragmentType.LIST)
        }

        setContentView(binding.root)
//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
//             IntentFilter("show_qr")
//        );
    }



    override fun gotCode(code: Long, delegate: DrivingsFragmentView) {
        presenter.testCode(code, delegate)
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                presenter.updateFragmentType(DrivingsListFragmentType.QR)
            } else {
                getCameraPermission()
            }


        }
    }

    private fun getCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.CAMERA
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            setFragmentByType(DrivingsListFragmentType.QR)
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                MapActivity.PERMISSIONS_REQUEST_ACCESS_CAMERA
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MapActivity.PERMISSIONS_REQUEST_ACCESS_CAMERA -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    presenter.updateFragmentType(DrivingsListFragmentType.QR)
                }
            }
        }
    }


    override fun showToast(string: String) {
        runOnUiThread {
            Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
        }
    }

    override fun setFragmentByType(type: DrivingsListFragmentType) {
        runOnUiThread {
            supportFragmentManager.beginTransaction().apply {
                when (type) {
                    DrivingsListFragmentType.QR -> {
//                        if (saveListFragment != null) {
//                            Log.d("DrivingsACtivity", "save list fragment not null")
//                            remove(saveListFragment!!)
//                        }
//
//                        if (saveCodeFragment != null) {
//                            Log.d("DrivingsACtivity", "saveCodeFragment not null")
//                            remove(saveCodeFragment!!)
//                        }

//                        if (saveQrFragment == null) {
//                            Log.d("DrivingsACtivity", "saveQrFragment == null")
//
//                        } else {
//                            Log.d("DrivingsACtivity", "saveQrFragment != null show")
//                            show(saveQrFragment!!)
//                        }

                            saveQrFragment = QRFragment(this@DrivingsActivity)
                            replace(R.id.drivings_frame, saveQrFragment!!)

                    }

                    DrivingsListFragmentType.CODE -> {
//                        if (saveListFragment != null) {
//                            remove(saveListFragment!!)
//                        }
//
//                        if (saveQrFragment != null)
//                            remove(saveQrFragment!!)

                        saveCodeFragment = DrivingsCodeFragment(this@DrivingsActivity)

                        replace(R.id.drivings_frame, saveCodeFragment!!)
                    }

                    DrivingsListFragmentType.LIST -> {
//                        if (saveQrFragment != null) {
//                            remove(saveQrFragment!!)
//                        }
//
//                        if (saveCodeFragment != null) {
//                            remove(saveCodeFragment!!)
//                        }
//
//                        if (saveListFragment == null) {
//
//                        } else {
//                            show(saveQrFragment!!)
//                        }

                        saveListFragment = DrivingsListFragment(this@DrivingsActivity)

                        replace(R.id.drivings_frame, saveListFragment!!)
                    }
                }
            }.commitAllowingStateLoss()
        }
    }

    override fun setResultCodeScooter(scooter: Scooter) {

        val data = Intent()
        data.putExtra("scooter", scooter as Serializable);
        setResult(Activity.RESULT_OK, data);
        finish()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onBackPressedByType(type: DrivingsListFragmentType) {
        runOnUiThread {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("DrivingsACtivity ", "Result ${requestCode} ${resultCode} ${data}")
    }

    override fun sendToCodeActivity() {
        runOnUiThread {
            presenter.returnedFromQRSendToCode()
        }
    }

    override fun toggleLantern() {
        runOnUiThread {
            if (saveQrFragment != null) {
                (saveQrFragment!! as QRFragment).toggleLantern()
            }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        presenter.onDestroy()
        super.onDestroy()
    }
}

enum class DrivingsStartTarget(val value: Int) {
    QRandCode(0), DrivingList(1)
}

enum class DrivingsListFragmentType {
    QR, CODE, LIST
}