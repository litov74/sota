package com.development.sota.scooter.ui.purse.presentation.fragments.transactions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.R
import com.development.sota.scooter.ui.purse.domain.entities.TransactionModel
import java.text.SimpleDateFormat
import java.util.*


class TransactionViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(
    inflater.inflate(
        R.layout.item_transaction,
        parent,
        false
    )
) {

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val dateFormatterTime = SimpleDateFormat("HH:mm", Locale.getDefault())
  //  private val dateFormatter = SimpleDateFormat("dd-M-yyyy hh:mm", Locale.getDefault())

    private var dateTransaction: TextView? = null
    private var cardTransaction: TextView? = null
    private var costTransaction: TextView? = null
    private var time: TextView? = null
    private var receiptContainer: View? = null
    init {
         dateTransaction =
                itemView.findViewById(R.id.dateTransaction)
         cardTransaction =
                itemView.findViewById(R.id.cardTransaction)
         costTransaction =
                itemView.findViewById(R.id.costTransaction)
        receiptContainer =
            itemView.findViewById(R.id.receiptContainer)
        time =
            itemView.findViewById(R.id.time)
    }
    fun bind(transactionModel: TransactionModel, context: Context) {
        dateTransaction?.text = dateFormatter.format(transactionModel.date_time)
        time?.text = dateFormatterTime.format(transactionModel.date_time)
        costTransaction?.text  = transactionModel.cost?.let { String.format("%.2f", it.toFloat()).plus(
            " р"
        ) }
        cardTransaction?.text  = "* "+transactionModel.last_four
        receiptContainer?.setOnClickListener(View.OnClickListener {
            var webpage = Uri.parse(transactionModel.receipt_link)

//            if (transactionModel.receipt_link?.startsWith("http://") != true && transactionModel.receipt_link?.startsWith("https://") != true) {
//                webpage = Uri.parse("https://${transactionModel.receipt_link}")
//            }

            val intent = Intent(Intent.ACTION_VIEW, webpage)
            if (intent.resolveActivity(context.getPackageManager()) != null) {
               context.startActivity(intent)
            }
        })

    }
}