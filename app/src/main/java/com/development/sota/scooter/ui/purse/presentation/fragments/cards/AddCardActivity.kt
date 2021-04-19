package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import android.os.Bundle
import android.util.Log
import android.util.Log.WARN
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.development.sota.scooter.R
import com.development.sota.scooter.common.Constants
import com.development.sota.scooter.databinding.FragmentAddCardBinding
import com.development.sota.scooter.databinding.FragmentUpBalanceBinding
import com.google.android.gms.common.util.Strings
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)
        return binding.root



    }

    override fun registerCard(){
        binding.buttonRegCard.setOnClickListener{
            if(binding.cardNumberInput.toString().length == 16){
                if(binding.cardDateInput.toString().length == 4){
                    if(binding.cardholderInput.toString() != ""){
                        if(binding.cardCodeInput.toString().length == 3){
                            val cardCryptogram = Card.cardCryptogram(binding.cardNumberInput.toString()
                                ,binding.cardDateInput.toString()
                                ,binding.cardCodeInput.toString()
                                ,Constants.MERCHANT_PUBLIC_ID)
                            if(cardCryptogram == null){
                                Log.e("Attempt to add card", "Error!!")
                                showToast("Пожалуйста, проверьте данные карты")
                            }else{

                            }
                        }
                    }
                }
            }

        }
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