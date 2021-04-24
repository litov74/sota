package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.R
import com.development.sota.scooter.ui.purse.domain.entities.AddCardModel
import com.development.sota.scooter.ui.purse.domain.entities.TransactionModel
import com.development.sota.scooter.ui.purse.domain.entities.UserCardModel
import java.text.SimpleDateFormat
import java.util.*

class UserCardViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_user_card, parent, false)) {

    private var cardLastSymbols: TextView? = null
    private var isMainCardContainer: LinearLayout? = null
    init {
        cardLastSymbols =
                itemView.findViewById(R.id.cardLastSymbols)
        isMainCardContainer =
                itemView.findViewById(R.id.isMainCardContainer)
    }
    fun bind(useCardModel: UserCardModel) {
        cardLastSymbols?.text = useCardModel.last_four
        if (useCardModel.is_main == true) {
            isMainCardContainer?.visibility = View.VISIBLE
        }
    }
}