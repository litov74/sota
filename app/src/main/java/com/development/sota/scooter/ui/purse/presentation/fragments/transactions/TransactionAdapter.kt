package com.development.sota.scooter.ui.purse.presentation.fragments.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.ui.purse.domain.entities.TransactionModel


class TransactionAdapter(private val list: List<TransactionModel>)
    : RecyclerView.Adapter<TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TransactionViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction: TransactionModel = list[position]
        holder.bind(transaction, holder.itemView.context)
    }

    override fun getItemCount(): Int = list.size
}