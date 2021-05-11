package com.development.sota.scooter.ui.drivings.presentation.fragments.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.R
import com.development.sota.scooter.ui.drivings.domain.entities.OrderStatus
import com.development.sota.scooter.ui.drivings.domain.entities.OrderWithStatus
import com.development.sota.scooter.ui.drivings.domain.entities.OrderWithStatusRate
import java.text.SimpleDateFormat
import java.util.*

class FinishedOrderAdapter(val data: List<OrderWithStatusRate>, val context: Context) :
    RecyclerView.Adapter<FinishedOrderAdapter.FinishedOrderViewHolder>() {
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FinishedOrderAdapter.FinishedOrderViewHolder = FinishedOrderViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_finished_order, parent, false)
    )

    override fun onBindViewHolder(
        holder: FinishedOrderAdapter.FinishedOrderViewHolder,
        position: Int
    ) =
        holder.itemView.run {
            holder.textViewItemFinishedOrderDate.text =
                dateFormatter.format(data[position].order.parseStartTime())
            holder.textViewItemFinishedOrderAmount.text =
                String.format("%.2f", data[position].order.cost).plus(" ₽")

            if (data[position].order.status == OrderStatus.CANCELED.value) {
                holder.textViewItemFinishedDelta.text =
                    context.getString(R.string.drivings_cancelled)
            }
            if (data[position].order.status == OrderStatus.CLOSED.value) {
                holder.textViewItemFinishedDelta.text =
                    context.getString(R.string.drivings_closed)
            }
        }

    override fun getItemCount(): Int = data.size

    inner class FinishedOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewItemFinishedOrderDate: TextView =
            itemView.findViewById(R.id.textViewItemFinishedOrderDate)
        val textViewItemFinishedOrderAmount: TextView =
            itemView.findViewById(R.id.textViewItemFinishedOrderAmount)
        val textViewItemFinishedDelta: TextView =
            itemView.findViewById(R.id.textViewItemFinishedDelta)
    }
}