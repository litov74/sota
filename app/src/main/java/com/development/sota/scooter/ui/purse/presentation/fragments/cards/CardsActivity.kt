package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.FragmentCardsBinding
import com.development.sota.scooter.databinding.FragmentTransactionsBinding
import com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd

interface CardsView : MvpView{
    @AddToEnd
    fun showToast(text: String)

    @AddToEnd
    fun showUserCards(last_four: String, main: String)


    @AddToEnd
    fun ShowProgress(boolean: Boolean)
}



class CardsActivity : MvpAppCompatFragment(R.layout.fragment_cards), CardsView{
    private val presenter by moxyPresenter {
        CardsPresenter(
            context ?: requireActivity().applicationContext
        )
    }
    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardsBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onDestroy() {
        presenter.onDestroyCalled()
        super.onDestroy()
    }

    override fun showToast(text: String) {
        runOnUiThread {

        }
    }

    override fun showUserCards(last_four: String, main: String) {

    }

    override fun ShowProgress(boolean: Boolean) {
        TODO("Not yet implemented")
    }


}