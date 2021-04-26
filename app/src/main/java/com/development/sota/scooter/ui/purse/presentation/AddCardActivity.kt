package com.development.sota.scooter.ui.purse.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.ActivityAddCardBinding
import com.development.sota.scooter.databinding.ActivityPurseBinding
import com.development.sota.scooter.ui.purse.presentation.fragments.cards.CardListFragment

import com.development.sota.scooter.ui.purse.presentation.fragments.transactions.TransactionsActivity
import com.development.sota.scooter.ui.purse.presentation.fragments.upbalance.UpBalanceActivity
import com.microsoft.appcenter.utils.HandlerUtils
import kotlinx.android.synthetic.main.activity_purse.*
import kotlinx.android.synthetic.main.fragment_cards.*
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd
import ru.cloudpayments.sdk.ui.dialogs.ThreeDsDialogFragment


interface IAddCardView: MvpView {
    @AddToEnd
    fun showToast(text: String)

    @AddToEnd
    fun setLoading(by: Boolean)

    @AddToEnd
    fun finish()

    @AddToEnd
    fun show3dSecure(AcsUrl: String, transactionId: Long, paReq: String)
}

class AddCardActivity : MvpAppCompatActivity(), IAddCardView, ThreeDsDialogFragment.ThreeDSDialogListener {

    private val presenter by moxyPresenter { AddCardPresenter(this) }

    private var _binding: ActivityAddCardBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.cardDateInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, start: Int, removed: Int, added: Int) {
                if (start == 1 && start+added == 2 && p0?.contains('/') == false) {
                    binding.cardDateInput.setText(p0.toString() + "/")
                    binding.cardDateInput.setSelection(binding.cardDateInput.text.toString().length)
                } else if (start == 3 && start-removed == 2 && p0?.contains('/') == true) {
                    binding.cardDateInput.setText(p0.toString().replace("/", ""))
                    binding.cardDateInput.setSelection(binding.cardDateInput.text.toString().length)
                }
            }
        })
        binding.buttonRegCard.setOnClickListener{
            presenter.attachCard(binding.cardNumberInput.text.toString(), binding.cardDateInput.text.toString(), binding.cardCodeInput.text.toString())
        }
        binding.imageButtonAddCardBack.setOnClickListener{
            finish()
        }
    }

    override fun showToast(text: String) {
        runOnUiThread {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun setLoading(by: Boolean) {
        runOnUiThread {
            if (by) {
                binding.progressAddCard.visibility = View.VISIBLE
                binding.buttonRegCard.visibility = View.GONE
            } else {
                binding.progressAddCard.visibility = View.GONE
                binding.buttonRegCard.visibility = View.VISIBLE
            }
        }
    }


    override fun show3dSecure(AcsUrl: String, transactionId: Long, paReq: String) {
        ThreeDsDialogFragment.newInstance(AcsUrl,
                paReq,
                transactionId.toString()).show(supportFragmentManager, "3DS")
    }

    override fun onAuthorizationCompleted(md: String, paRes: String) {
        presenter.success3dSecure(md, paRes)
    }

    override fun onAuthorizationFailed(error: String?) {
        presenter.error3dSecure()

    }


}
