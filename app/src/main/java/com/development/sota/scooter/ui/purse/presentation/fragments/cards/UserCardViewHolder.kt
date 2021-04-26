package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.R
import com.development.sota.scooter.ui.purse.domain.entities.UserCardModel

class UserCardViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_user_card, parent, false)) {

    private var cardLastSymbols: TextView? = null
    private var isMainCardContainer: LinearLayout? = null
    private var isNotMainCardEditContainer: LinearLayout? = null
    private var isMainCardEditContainer: LinearLayout? = null
    private var removeMainCard: View? = null
    private var removeNotMainCard: View? = null
    private var setMainCard: View? = null
    private var editMode: Boolean = false
    private val userCardAction: UserCardAction? = null

    init {
        cardLastSymbols =
                itemView.findViewById(R.id.cardLastSymbols)
        isMainCardContainer =
                itemView.findViewById(R.id.isMainCardContainer)
        isNotMainCardEditContainer =
                itemView.findViewById(R.id.isNotMainCardEditContainer)
        isMainCardEditContainer =
                itemView.findViewById(R.id.isMainCardEditContainer)
        removeNotMainCard =
                itemView.findViewById(R.id.removeNotMainCard)
        removeMainCard =
                itemView.findViewById(R.id.removeMainCard)
        setMainCard =
                itemView.findViewById(R.id.setMainCard)
    }
    fun bind(useCardModel: UserCardModel, editMode: Boolean, userCardAction: UserCardAction) {
        cardLastSymbols?.text = useCardModel.last_four
        isMainCardContainer?.visibility = View.GONE
        isNotMainCardEditContainer?.visibility = View.GONE
        isMainCardEditContainer?.visibility = View.GONE

        if (editMode) {
            if (useCardModel.is_main == true) {
                isMainCardEditContainer?.visibility = View.VISIBLE
            } else {
                isNotMainCardEditContainer?.visibility = View.VISIBLE
            }
        } else if (useCardModel.is_main == true) {
            isMainCardContainer?.visibility = View.VISIBLE
        }

        removeNotMainCard?.setOnClickListener { userCardAction.selectRemove(useCardModel) }
        removeMainCard?.setOnClickListener { userCardAction.selectRemove(useCardModel) }
        setMainCard?.setOnClickListener { userCardAction.setMain(useCardModel) }
    }
}