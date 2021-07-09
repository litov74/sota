package com.development.sota.scooter.ui.purse.presentation.fragments.upbalance

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.FragmentUpBalanceBinding
import com.development.sota.scooter.ui.purse.domain.entities.UpBalancePackageModel
import com.development.sota.scooter.ui.purse.presentation.WalletActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd
import org.json.JSONArray
import org.json.JSONObject


interface UpBalanceView : MvpView {

    @AddToEnd
    fun showProgress(by: Boolean)

    @AddToEnd
    fun setListPackages(list: List<UpBalancePackageModel>)

    @AddToEnd
    fun updateUserBalance()

    @AddToEnd
    fun showToast(message: String)

    @AddToEnd
    fun showAlertPayment(cost: String)

    @AddToEnd
    fun showPaymentTypeDialog(cost: String)

    @AddToEnd
    fun showUpBalanceOk(income: String)
}

interface UpBalanceManipulatorDelegate {
    fun selectPackage(model: UpBalancePackageModel)

}

class UpBalanceActivity: MvpAppCompatFragment(R.layout.fragment_up_balance), UpBalanceView, UpBalanceManipulatorDelegate {
    private val presenter by moxyPresenter {
        UpBalancePresenter(
            context ?: requireActivity().applicationContext
        )
    }
    private var _binding: FragmentUpBalanceBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: MaterialDialog
    private lateinit var paymentsClient: PaymentsClient
    private val baseCardPaymentMethod = JSONObject().apply {
        put("type", "CARD")
        put("parameters", JSONObject().apply {
            put("allowedCardNetworks", JSONArray(listOf("VISA", "MASTERCARD")))
            put("allowedAuthMethods", JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS")))
        })
    }
    private val googlePayBaseConfiguration = JSONObject().apply {
        put("apiVersion", 2)
        put("apiVersionMinor", 0)
        put("allowedPaymentMethods",  JSONArray().put(baseCardPaymentMethod))
    }
    private val tokenizationSpecification = JSONObject().apply {
        put("type", "PAYMENT_GATEWAY")
        put("parameters", JSONObject(mapOf(
            "gateway" to "example",
            "gatewayMerchantId" to "exampleGatewayMerchantId")))
    }
    private val cardPaymentMethod = JSONObject().apply {
        put("type", "CARD")
        put("tokenizationSpecification", tokenizationSpecification)
        put("parameters", JSONObject().apply {
            put("allowedCardNetworks", JSONArray(listOf("VISA", "MASTERCARD")))
            put("allowedAuthMethods", JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS")))
            put("billingAddressRequired", true)
            put("billingAddressParameters", JSONObject(mapOf("format" to "FULL")))
        })
    }
    private val merchantInfo = JSONObject().apply {
        put("merchantName", "Example Merchant")
        put("merchantId", "01234567890123456789")
    }
    private val transactionInfo = JSONObject().apply {
        put("totalPrice", "123.45")
        put("totalPriceStatus", "FINAL")
        put("currencyCode", "RUB")
    }
    private val paymentDataRequestJson = JSONObject(googlePayBaseConfiguration.toString()).apply {
        put("allowedPaymentMethods", JSONArray().put(cardPaymentMethod))
        put("transactionInfo", transactionInfo)
        put("merchantInfo", merchantInfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpBalanceBinding.inflate(inflater, container, false)
        binding.listPacks.layoutManager = GridLayoutManager(context, 2)
        return binding.root
    }

    override fun onDestroy() {
        presenter.onDestroyCalled()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        presenter.resumeView()
    }

    override fun showProgress(by: Boolean) {
        runOnUiThread {
            if (by) {
                binding.listPacks.visibility = View.GONE
                binding.progressLoading.visibility = View.VISIBLE
            } else {
                binding.listPacks.visibility = View.VISIBLE
                binding.progressLoading.visibility = View.GONE
            }

        }
    }

    override fun setListPackages(list: List<UpBalancePackageModel>) {
        runOnUiThread {
                val grid = GridLayoutManager(context, 2)

                binding.listPacks.layoutManager = grid
                binding.listPacks.adapter = UpBalanceAdapter(list.sortedBy { it.cost }, this)
        }
    }

    override fun updateUserBalance() {
        (context as WalletActivity).needUpdateBalance()
    }

    override fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showAlertPayment(cost: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_attention)
            .setMessage("Подтвердите покупку пакета за $cost руб и на старт!")
            .setPositiveButton("Подтверждаю") {
                    dialog, id ->  dialog.cancel()
                    presenter.confirmUpBalance()
            }
            .setNegativeButton("Отмена") {
                    dialog, id ->  dialog.cancel()
            }
            .create()
            .show()
    }

    fun createPaymentsClient(activity: Activity): PaymentsClient {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST).build()
        return Wallet.getPaymentsClient(activity, walletOptions)
    }

    private fun setGooglePayAvailable(available: Boolean) {
        if (available) {
            val dialog = BottomSheetDialog(requireContext())
            var view: View = layoutInflater.inflate(R.layout.layout_payment_dialog, null)
            val btnClose = view.findViewById<ConstraintLayout>(R.id.googlePayContainer)
            btnClose.setOnClickListener {
                requestPayment()
                dialog.dismiss()
            }
            dialog.setContentView(view)
            dialog.show()

        } else {

        }
    }

    private fun requestPayment() {
        val paymentDataRequest =
            PaymentDataRequest.fromJson(paymentDataRequestJson.toString())

        AutoResolveHelper.resolveTask(
            paymentsClient.loadPaymentData(paymentDataRequest),
            requireActivity(), 123)
    }

    override fun showPaymentTypeDialog(cost: String) {
        paymentsClient = createPaymentsClient(requireActivity())
        val readyToPayRequest =
            IsReadyToPayRequest.fromJson(googlePayBaseConfiguration.toString())

        val readyToPayTask = paymentsClient.isReadyToPay(readyToPayRequest)
        readyToPayTask.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
            } catch (exception: ApiException) {

            }
        }

        val params = PaymentMethodTokenizationParameters.newBuilder()
            .setPaymentMethodTokenizationType(
                WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY
            )
            .addParameter("gateway", "cloudpayments")
            .addParameter("gatewayMerchantId", "Ваш Public ID")
            .build()


    }

    override fun showUpBalanceOk(income: String) {
        requireActivity().runOnUiThread({
            AlertDialog.Builder(requireContext())
                .setTitle("Уведомление")
                .setMessage("Баланс пополнен на $income руб ")
                .setPositiveButton("Ок") {
                        dialog, id ->  dialog.cancel()
                }
                .create()
                .show()
        })

    }

    override fun selectPackage(model: UpBalancePackageModel) {
        presenter.upBalancePackage(model)
    }

}