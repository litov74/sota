package com.development.sota.scooter.ui.purse.presentation.fragments.upbalance

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
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
    private var root: View? = null
    init {
        cost = itemView.findViewById(R.id.cost)
        benefit = itemView.findViewById(R.id.benefit)
        income = itemView.findViewById(R.id.income)
        container = itemView.findViewById(R.id.upBalanceContainer)
        root = itemView.findViewById(R.id.root)
    }

    fun bind(model: UpBalancePackageModel, upBalanceManipulatorDelegate: UpBalanceManipulatorDelegate, position: Int, context: Context) {

        val benefitValue = model.income - model.cost

        when (position) {
           0 -> {
                root?.background = context.getDrawable(R.drawable.background_green_gradient)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    income?.setTextColor(context.getColor(R.color.text_green))
                }
            }
            1 -> {
                root?.background = context.getDrawable(R.drawable.background_red_gradient)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    income?.setTextColor(context.getColor(R.color.red))
                }
            }
          2 -> {
                root?.background = context.getDrawable(R.drawable.background_yellow_gradient)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    income?.setTextColor(context.getColor(R.color.yellow))
                }
            }
            3 -> {
                root?.background = context.getDrawable(R.drawable.background_blue_gradient)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    income?.setTextColor(context.getColor(R.color.blue))
                }
            }
        }
        cost?.text  = model.cost?.let { String.format("%.2f", it.toFloat()).plus("р") }
        benefit?.text = "Выгода "+benefitValue.toString()+"р"
        income?.text = model.income?.let { String.format("%.2f", it.toFloat()).plus("р") }
        container?.setOnClickListener({
            upBalanceManipulatorDelegate.selectPackage(model)
        })


    }
}