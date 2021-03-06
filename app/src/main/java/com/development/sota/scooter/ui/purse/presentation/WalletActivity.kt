package com.development.sota.scooter.ui.purse.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.ActivityPurseBinding
import com.development.sota.scooter.ui.purse.presentation.fragments.cards.CardListFragment

import com.development.sota.scooter.ui.purse.presentation.fragments.transactions.TransactionsActivity
import com.development.sota.scooter.ui.purse.presentation.fragments.upbalance.UpBalanceActivity
import com.google.android.gms.wallet.PaymentData
import kotlinx.android.synthetic.main.activity_purse.*
import kotlinx.android.synthetic.main.fragment_cards.*
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd
import org.json.JSONException
import org.json.JSONObject


interface WalletView: MvpView {
    @AddToEnd
    fun showToast(text: String)

    @AddToEnd
    fun setLoading(by: Boolean)

    @AddToEnd
    fun setUserBalance(value: String)
}

interface WalletActivityView {

    fun needUpdateBalance()

    fun setCardCount(count: Int)
}

class WalletActivity : MvpAppCompatActivity(), WalletView, WalletActivityView{

    private val presenter by moxyPresenter { WalletPresenter(this) }

    private var _binding: ActivityPurseBinding? = null
    private val binding get() = _binding!!
    private var cardListFragment: CardListFragment? = null
    private var upBalanceActivity: UpBalanceActivity? = null
    var key: Int = 0
    val fm = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPurseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageButtonDrivingsListBack.setOnClickListener{
            onBackPressed()
        }



        upBalanceActivity = UpBalanceActivity()
        fm.beginTransaction().add(binding.host.id, upBalanceActivity!!).commit()
        binding.btnOpenUpBalance.setOnClickListener{
            if(key != 0){
                binding.editCardsContainer.visibility = View.GONE
                unCheck(binding.transactionsProfile)
                unCheck(binding.cardsProfile)
                binding.topUpProfile.setColorFilter(ContextCompat.getColor(this, R.color.selected_profile_menu_tint), android.graphics.PorterDuff.Mode.SRC_IN);
                key = 0
                fm.beginTransaction().replace(binding.host.id, UpBalanceActivity()).commit()
            }
        }
        binding.btnOpenCards.setOnClickListener{
            if(key != 1){
                binding.editCardsContainer.visibility = View.GONE
                unCheck(binding.transactionsProfile)
                binding.cardsProfile.setColorFilter(ContextCompat.getColor(this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                unCheck(binding.topUpProfile)
                key = 1
                cardListFragment = CardListFragment()
                fm.beginTransaction().replace(binding.host.id, cardListFragment!!).commit()
            }
        }
        binding.btnOpenTransactions.setOnClickListener{
            if(key != 2){
                binding.editCardsContainer.visibility = View.GONE
                binding.transactionsProfile.setColorFilter(ContextCompat.getColor(this, R.color.text_green), android.graphics.PorterDuff.Mode.SRC_IN);
                unCheck(binding.cardsProfile)
                unCheck(binding.topUpProfile)
                key = 2
                fm.beginTransaction().replace(binding.host.id, TransactionsActivity()).commit()
            }
        }
//        binding.addCardTextView.setOnClickListener {
//            fm.beginTransaction().replace(binding.host.id, AddCard()).commit()
//            addCardTextView.visibility = View.GONE
//        }


        binding.editCardsContainer.setOnClickListener { cardListFragment?.changeEditMode() }
    }


    fun openAttachCard() {
        binding.editCardsContainer.visibility = View.GONE
        unCheck(binding.transactionsProfile)
        binding.cardsProfile.setColorFilter(ContextCompat.getColor(this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
        unCheck(binding.topUpProfile)
        key = 1
        cardListFragment = CardListFragment()
        fm.beginTransaction().replace(binding.host.id, cardListFragment!!).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            123 -> when (resultCode) {
                Activity.RESULT_OK -> {
                    val paymentData = PaymentData.getFromIntent(data!!)
                    paymentData?.let { handlePaymentSuccess(it) }
                }
                Activity.RESULT_CANCELED -> {
                }
            }
            else -> {
            }
        }
    }

    private fun handlePaymentSuccess(paymentData: PaymentData) {
        val paymentInformation = paymentData.toJson() ?: return

        try {

            Log.d("WalletActivity", paymentData.toJson())

            val paymentMethodData = JSONObject(paymentInformation).getJSONObject("paymentMethodData")

            Log.d("GooglePaymentToken", paymentMethodData
                .getJSONObject("tokenizationData")
                .getString("token"))

            upBalanceActivity?.setPaymentToken( paymentMethodData
                .getJSONObject("tokenizationData")
                .getString("token"))


        } catch (e: JSONException) {
            Log.e("handlePaymentSuccess", "Error: " + e.toString())
        }

    }

    private fun unCheck(mageView: ImageView){
        mageView.setColorFilter(ContextCompat.getColor(this, R.color.profile_tint_gray), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private fun check(imageView: ImageView){
        imageView.setColorFilter(ContextCompat.getColor(this, R.color.selected_profile_menu_tint), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    override fun setUserBalance(value: String) {
        (binding.balance as TextView).text = "$value ?? "

    }

    override fun showToast(text: String) {
        runOnUiThread {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun setLoading(by: Boolean) {
        runOnUiThread {
          //  binding.progressBarDrivingsList.visibility = if (by) View.VISIBLE else View.GONE
        }
    }

    override fun needUpdateBalance() {
        presenter.updateUserBalance()
    }

    override fun setCardCount(count: Int) {
        if (count > 1) {
            binding.editCardsContainer.visibility = View.VISIBLE
        } else {
            binding.editCardsContainer.visibility = View.GONE
            cardListFragment?.cancelEditMode()
        }
    }
}
