package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.ui.purse.domain.entities.TransactionModel
import com.development.sota.scooter.ui.purse.domain.entities.UserCardModel
import com.development.sota.scooter.ui.purse.presentation.fragments.transactions.TransactionViewHolder


class UserCardAdapter(private val list: List<UserCardModel>) : RecyclerView.Adapter<UserCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserCardViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: UserCardViewHolder, position: Int) {
        val userCardModel: UserCardModel = list[position]
        holder.bind(userCardModel)
    }

    override fun getItemCount(): Int = list.size
}