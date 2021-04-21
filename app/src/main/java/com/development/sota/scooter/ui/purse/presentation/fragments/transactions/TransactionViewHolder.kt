package com.development.sota.scooter.ui.purse.presentation.fragments.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.R
import com.development.sota.scooter.ui.purse.domain.entities.TransactionModel
import java.text.SimpleDateFormat
import java.util.*

class TransactionViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_transaction, parent, false)) {

    private val dateFormatter = SimpleDateFormat("dd-M-yyyy hh:mm", Locale.getDefault())

    private var dateTransaction: TextView? = null
    private var cardTransaction: TextView? = null
    private var costTransaction: TextView? = null
    init {
         dateTransaction =
                itemView.findViewById(R.id.dateTransaction)
         cardTransaction =
                itemView.findViewById(R.id.cardTransaction)
         costTransaction =
                itemView.findViewById(R.id.costTransaction)
    }
    fun bind(transactionModel: TransactionModel) {
        dateTransaction?.text = dateFormatter.format(transactionModel.date_time)
        costTransaction?.text  = transactionModel.cost?.let { String.format("%.2f", it.toFloat()).plus(" ₽") }
        cardTransaction?.text  = transactionModel.used_card
    }
}