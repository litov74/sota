package com.development.sota.scooter.ui.drivings.presentation.fragments.qr

import com.development.sota.scooter.common.base.BasePresenter
import com.development.sota.scooter.ui.drivings.presentation.fragments.QRView
import moxy.MvpPresenter

class QRPresenter : MvpPresenter<QRView>(), BasePresenter {
    private var errorCount = 0



    override fun onDestroyCalled() {

    }

    fun gotErrorFromScanner() {
        if (++errorCount >= 3) {
            viewState.sendToCodeFragment()
        }
    }

    fun getDataFromScanner(data: String) {

        val parsed = getQRId(data)

        val parsedInt = parsed.toIntOrNull()
        if (parsedInt != null) {

            viewState.sendFoundCodeToDrivings(parsed.toLong())
            viewState.setLoading(true)
        } else {
            viewState.setLoading(false)
            gotErrorFromScanner()
        }

    }

    fun gotResponseFromActivity(result: Boolean) {
        if (result) {
            viewState.setLoading(false)
        } else {
            viewState.setLoading(false)
            viewState.sendToCodeFragment()
        }
    }

    // If fails - return null
    // Example: scooter.sota/qr?id=1234
    private fun parseQRData(data: String): String? {
        val index = data.indexOf("id=")

        if (index == -1) {
            return null
        } else {
            return data.substringAfter("id=")
        }
    }

    private fun getQRId(data: String): String {
        return data.split("qr/").getOrNull(1).toString()
    }
}