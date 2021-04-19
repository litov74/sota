package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.FragmentAddCardBinding
import com.development.sota.scooter.databinding.FragmentUpBalanceBinding
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd

interface AddCardActivity: MvpView{}





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

    override fun onDestroy() {
        presenter.onDestroyCalled()
        super.onDestroy()
    }



}