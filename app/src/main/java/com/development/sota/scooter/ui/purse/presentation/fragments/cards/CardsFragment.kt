package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import com.development.sota.scooter.R
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd

interface CardsView: MvpView{

}

class CardsFragment: MvpAppCompatFragment(R.layout.fragment_cards), CardsView{



}