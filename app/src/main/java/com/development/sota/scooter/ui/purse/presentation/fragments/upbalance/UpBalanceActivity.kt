package com.development.sota.scooter.ui.purse.presentation.fragments.upbalance

import com.development.sota.scooter.R
import moxy.MvpAppCompatFragment
import moxy.MvpView


interface TransactionView : MvpView {}


class UpBalanceActivity: MvpAppCompatFragment(R.layout.fragment_up_balance), TransactionView {
}