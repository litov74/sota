package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.FragmentCardsBinding
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter

interface CardsView : MvpView {}



class CardsActivity : MvpAppCompatFragment(R.layout.fragment_cards), CardsView{
    private val presenter by moxyPresenter {
        CardsPresenter(
            context ?: activity!!.applicationContext
        )
    }
    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!
}