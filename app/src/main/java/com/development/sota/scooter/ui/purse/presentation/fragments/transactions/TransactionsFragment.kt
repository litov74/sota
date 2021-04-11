package com.development.sota.scooter.ui.purse.presentation.fragments.transactions

import com.development.sota.scooter.R
import moxy.MvpAppCompatFragment
import moxy.MvpView

interface TransactionsView : MvpView {}

class TransactionsFragment: MvpAppCompatFragment(R.layout.fragment_transactions), TransactionsView{
}