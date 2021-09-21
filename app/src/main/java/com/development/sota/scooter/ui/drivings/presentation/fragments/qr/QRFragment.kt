package com.development.sota.scooter.ui.drivings.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.ScanMode
import com.development.sota.scooter.databinding.FragmentDrivingsQrBinding
import com.development.sota.scooter.ui.drivings.presentation.DrivingsActivityView
import com.development.sota.scooter.ui.drivings.presentation.DrivingsFragmentView
import com.development.sota.scooter.ui.drivings.presentation.DrivingsListFragmentType
import com.development.sota.scooter.ui.drivings.presentation.fragments.qr.QRPresenter
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd

interface QRView : MvpView {
    @AddToEnd
    fun sendFoundCodeToDrivings(code: Long)

    @AddToEnd
    fun setLoading(by: Boolean)

    @AddToEnd
    fun sendToCodeFragment()
}

class QRFragment(val drivingsView: DrivingsActivityView) : MvpAppCompatFragment(), QRView,
    DrivingsFragmentView {
    private val presenter by moxyPresenter { QRPresenter() }

    private var _binding: FragmentDrivingsQrBinding? = null
    private val binding get() = _binding!!

    private  var codeScanner: CodeScanner? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("QRFragment", "new fragment")
        _binding = FragmentDrivingsQrBinding.inflate(inflater, container, false)

        initScanner()

        binding.imageButtonQrScannerLantern.setOnClickListener {
            codeScanner!!.isFlashEnabled = !codeScanner!!.isFlashEnabled
        }

        binding.imageButtonQrScannerCode.setOnClickListener {
            drivingsView.sendToCodeActivity()
        }

        binding.imageButtonQrScannerBack.setOnClickListener {
            drivingsView.onBackPressedByType(DrivingsListFragmentType.QR)
        }

        return binding.root
    }

    private fun initScanner() {
        try {
            codeScanner = CodeScanner(requireContext(), binding.qrScanner)
            codeScanner!!.setDecodeCallback {
                presenter.getDataFromScanner(it.text)
            }
            codeScanner!!.setErrorCallback {
                presenter.gotErrorFromScanner()
            }

            // Parameters (default values)
            codeScanner!!.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
            codeScanner!!.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
            // ex. listOf(BarcodeFormat.QR_CODE)
            codeScanner!!.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
            codeScanner!!.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
            codeScanner!!.isAutoFocusEnabled = true // Whether to enable auto focus or not
            codeScanner!!.isFlashEnabled = false // Whether to enable flash or

            codeScanner!!.autoFocusMode = AutoFocusMode.CONTINUOUS
            codeScanner!!.isAutoFocusEnabled = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun sendFoundCodeToDrivings(code: Long) {
        activity?.runOnUiThread {
            drivingsView.gotCode(code, this)
        }
    }

    override fun setLoading(by: Boolean) {
//        activity?.runOnUiThread {
//            if (by) {
//                binding.progressBarDrivingsQr.visibility = View.VISIBLE
//                codeScanner.stopPreview()
//            } else {
//                binding.progressBarDrivingsQr.visibility = View.GONE
//                codeScanner.startPreview()
//            }
//        }
    }



    override fun onResume() {
        super.onResume()
        try {
            activity?.runOnUiThread {
                codeScanner!!.startPreview()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    override fun onPause() {
        try {

            codeScanner!!.releaseResources()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        super.onPause()
    }

    fun toggleLantern() {
        activity?.runOnUiThread {
            codeScanner!!.isFlashEnabled = !codeScanner!!.isFlashEnabled
        }
    }

    override fun sendToCodeFragment() {
        activity?.runOnUiThread {
            codeScanner!!.stopPreview()

            drivingsView.sendToCodeActivity()
        }
    }

    override fun gotResultOfCodeChecking(result: Boolean) {
        presenter.gotResponseFromActivity(result)
    }


}