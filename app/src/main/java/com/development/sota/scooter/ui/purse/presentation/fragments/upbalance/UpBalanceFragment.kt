package com.development.sota.scooter.ui.purse.presentation.fragments.upbalance

import android.icu.util.DateInterval
import com.development.sota.scooter.R
import com.development.sota.scooter.ui.purse.presentation.fragments.transactions.TransactionsView
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd

interface UpBalance: MvpView{}

class UpBalanceFragment: MvpAppCompatFragment(R.layout.fragment_up_balance), UpBalance {



}