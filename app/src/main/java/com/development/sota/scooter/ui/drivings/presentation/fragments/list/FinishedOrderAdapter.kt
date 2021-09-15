package com.development.sota.scooter.ui.drivings.presentation.fragments.list

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.R
import com.development.sota.scooter.ui.drivings.domain.entities.OrderStatus
import com.development.sota.scooter.ui.drivings.domain.entities.OrderWithStatusRate
import org.joda.time.Period
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetDateTime
import java.util.*

class FinishedOrderAdapter(val data: List<OrderWithStatusRate>, val context: Context) :
    RecyclerView.Adapter<FinishedOrderAdapter.FinishedOrderViewHolder>() {
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FinishedOrderAdapter.FinishedOrderViewHolder = FinishedOrderViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_finished_order, parent, false)
    )

    fun findDifference(start_date: String?, end_date: String?): Long {
        val sdf = SimpleDateFormat("yyyyMM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        var difference_In_Minutes: Long = 0

        try {

            val d1 = sdf.parse(start_date)
            val d2 = sdf.parse(end_date)

            val difference_In_Time = d2.time - d1.time



            val difference_In_Hours = ((difference_In_Time
                    / (1000 * 60 * 60))
                    % 24)

             difference_In_Minutes = ((difference_In_Time
                    / (1000 * 60))
                    % 60)
        }

        catch (e: ParseException) {
            e.printStackTrace()
        }
        finally {
            return difference_In_Minutes
        }
    }

    override fun onBindViewHolder(
        holder: FinishedOrderAdapter.FinishedOrderViewHolder,
        position: Int
    ) =
        holder.itemView.run {

            var period = Period(data[position].order.parseStartTime()!!, data[position].order.parseEndTime()!!)


            val odt = OffsetDateTime.parse(data[position].order.startTime)

            holder.textViewItemFinishedOrderDate.text = dateFormatter.format(data[position].order.parseStartTimeWithoutOffset())+" "+period.minutes+" m "+period.seconds+" s"

            holder.textViewItemFinishedOrderAmount.text =
                String.format("%.2f", data[position].order.cost).plus(" р")

            holder.scooterNumber.text = "Самокат №"+data[position].order.scooter

            if (data[position].order.status == OrderStatus.CLOSED.value) {
                holder.textViewItemFinishedDelta.setImageDrawable(resources.getDrawable(R.drawable.ic_accept))
            }
            if (data[position].order.status == OrderStatus.CANCELED.value) {
                holder.textViewItemFinishedDelta.setImageDrawable(resources.getDrawable(R.drawable.ic_cancel))
            }
        }

    override fun getItemCount(): Int = data.size

    inner class FinishedOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewItemFinishedOrderDate: TextView =
            itemView.findViewById(R.id.textViewItemFinishedOrderDate)
        val textViewItemFinishedOrderAmount: TextView =
            itemView.findViewById(R.id.textViewItemFinishedOrderAmount)
        val textViewItemFinishedDelta: ImageView =
            itemView.findViewById(R.id.textViewItemFinishedDelta)
        val scooterNumber: TextView =
            itemView.findViewById(R.id.scooterNumber)
    }
}