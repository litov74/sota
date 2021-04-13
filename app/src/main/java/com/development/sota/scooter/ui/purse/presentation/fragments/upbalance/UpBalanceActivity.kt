package com.development.sota.scooter.ui.purse.presentation.fragments.upbalance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.FragmentUpBalanceBinding
import com.development.sota.scooter.ui.purse.presentation.fragments.cards.CardsPresenter
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter


interface UpBalanceView : MvpView {}


class UpBalanceActivity: MvpAppCompatFragment(R.layout.fragment_up_balance), UpBalanceView {
    private val presenter by moxyPresenter {
        CardsPresenter(
            context ?: requireActivity().applicationContext
        )
    }
    private var _binding: FragmentUpBalanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpBalanceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroy() {
        presenter.onDestroyCalled()
        super.onDestroy()
    }

}