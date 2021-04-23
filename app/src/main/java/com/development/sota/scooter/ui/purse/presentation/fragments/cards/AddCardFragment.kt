//package com.development.sota.scooter.ui.purse.presentation.fragments.cards
//
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import com.development.sota.scooter.R
//import com.development.sota.scooter.common.Constants
//import com.development.sota.scooter.databinding.ActivityAddCardBinding
//import com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread
//import moxy.MvpAppCompatFragment
//import moxy.MvpView
//import moxy.ktx.moxyPresenter
//import moxy.viewstate.strategy.alias.AddToEnd
//import ru.cloudpayments.sdk.card.Card
//
//
//interface IAddCard: MvpView{
//
//    @AddToEnd
//    fun registerCard()
//
//    @AddToEnd
//    fun showToast(text: String)
//
//    @AddToEnd
//    fun showProgress(boolean: Boolean)
//}
//
//
//class AddCardFragment : MvpAppCompatFragment(R.layout.activity_add_card), IAddCard {
//
//    private val presenter by moxyPresenter {
//        AddCardPresenter(
//                context ?: requireActivity().applicationContext
//        )
//    }
//
//    private var _binding: ActivityAddCardBinding? = null
//    private val binding get() = _binding!!
//
//
//    private fun initView() {
//        binding.cardDateInput.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {}
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//            override fun onTextChanged(p0: CharSequence?, start: Int, removed: Int, added: Int) {
//                if (start == 1 && start+added == 2 && p0?.contains('/') == false) {
//                    binding.cardDateInput.setText(p0.toString() + "/")
//                } else if (start == 3 && start-removed == 2 && p0?.contains('/') == true) {
//                    binding.cardDateInput.setText(p0.toString().replace("/", ""))
//                }
//            }
//        })
//        binding.buttonRegCard.setOnClickListener{
//
//            presenter.attachCard(binding.cardNumberInput.text.toString(), binding.cardDateInput.text.toString(), binding.cardCodeInput.text.toString())
//
//
//        }
//    }
//
//    override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        _binding = ActivityAddCardBinding.inflate(inflater, container, false)
//        initView()
//        return binding.root
//
//    }
//
//    override fun registerCard(){
//        runOnUiThread {
//
//        }
//    }
//
//
//    override fun onDestroy() {
//        presenter.onDestroyCalled()
//        super.onDestroy()
//    }
//
//    override fun showToast(text: String) {
//        runOnUiThread {
//            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun showProgress(boolean: Boolean) {
//        runOnUiThread {
//            if (boolean) {
//                this.binding.loadingContainer.visibility = View.VISIBLE
//                this.binding.contentContainer.visibility = View.GONE
//            } else {
//                this.binding.loadingContainer.visibility = View.GONE
//                this.binding.contentContainer.visibility = View.VISIBLE
//            }
//        }
//    }
//
//
//}