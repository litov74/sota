package com.development.sota.scooter.ui.purse.presentation.fragments.upbalance

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.R
import com.development.sota.scooter.ui.purse.domain.entities.TransactionModel
import com.development.sota.scooter.ui.purse.domain.entities.UpBalancePackageModel
import java.text.SimpleDateFormat
import java.util.*

class UpBalanceViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_purse, parent, false)) {



    private var cost: TextView? = null
    private var benefit: TextView? = null
    private var income: TextView? = null
    private var container: CardView? = null
    init {
        cost = itemView.findViewById(R.id.cost)
        benefit = itemView.findViewById(R.id.benefit)
        income = itemView.findViewById(R.id.income)
        container = itemView.findViewById(R.id.upBalanceContainer)
    }
    fun bind(model: UpBalancePackageModel, upBalanceManipulatorDelegate: UpBalanceManipulatorDelegate) {

        val benefitValue = model.income - model.cost

        cost?.text  = model.cost?.let { String.format("%.2f", it.toFloat()).plus(" ₽") }
        benefit?.text = "Выгода "+benefitValue.toString()+"  ₽"
        income?.text = model.income?.let { String.format("%.2f", it.toFloat()).plus(" ₽") }
        container?.setOnClickListener({
            upBalanceManipulatorDelegate.selectPackage(model.id)
        })


    }
}