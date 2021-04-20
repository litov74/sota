package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.development.sota.scooter.R
import com.development.sota.scooter.common.Constants
import com.development.sota.scooter.databinding.FragmentAddCardBinding
import com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread
import kotlinx.android.synthetic.main.fragment_add_card.*
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd
import ru.cloudpayments.sdk.card.Card


interface AddCardActivity: MvpView{
    @AddToEnd
    fun registerCard()

    @AddToEnd
    fun showToast(text: String)
}


class AddCard : MvpAppCompatFragment(R.layout.fragment_add_card), AddCardActivity{

    private val presenter by moxyPresenter {
        AddCardPresenter(
                context ?: requireActivity().applicationContext
        )
    }

    private var _binding: FragmentAddCardBinding? = null
    private val binding get() = _binding!!


    private fun initView() {
        binding.cardDateInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, start: Int, removed: Int, added: Int) {
                if (start == 1 && start+added == 2 && p0?.contains('/') == false) {
                    binding.cardDateInput.setText(p0.toString() + "/")
                } else if (start == 3 && start-removed == 2 && p0?.contains('/') == true) {
                    binding.cardDateInput.setText(p0.toString().replace("/", ""))
                }
            }
        })
        binding.buttonRegCard.setOnClickListener{

            System.out.println(binding.cardNumberInput.text.toString())
            System.out.println(Card.isValidNumber(binding.cardNumberInput.text.toString()))
            System.out.println(Card.isValidExpDate(binding.cardDateInput.text.toString()))
            System.out.println(Card.getType(binding.cardNumberInput.text.toString()))

            if (Card.isValidNumber(binding.cardNumberInput.text.toString()) && Card.isValidExpDate(binding.cardDateInput.text.toString())) {
                val cardCryptogram = Card.cardCryptogram(binding.cardNumberInput.toString(), binding.cardDateInput.toString(), binding.cardCodeInput.text.toString(), Constants.MERCHANT_PUBLIC_ID)
                if(cardCryptogram == null){
                    Log.e("Attempt to add card", "Error!!")
                    showToast("Пожалуйста, проверьте данные карты")

                }else{
                    showToast("ВСе ок ")
                }
            } else {
                showToast("Пожалуйста, проверьте данные карты")
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)
        initView()
        return binding.root

    }

    override fun registerCard(){

    }


    override fun onDestroy() {
        presenter.onDestroyCalled()
        super.onDestroy()
    }

    override fun showToast(text: String) {
        runOnUiThread {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }



}