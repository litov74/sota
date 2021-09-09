package com.development.sota.scooter.ui.drivings.presentation.fragments.list

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.R
import com.development.sota.scooter.ui.drivings.domain.entities.OrderStatus
import com.development.sota.scooter.ui.drivings.domain.entities.OrderWithStatus
import com.development.sota.scooter.ui.drivings.domain.entities.OrderWithStatusRate
import com.development.sota.scooter.ui.drivings.presentation.DrivingsActivity
import com.development.sota.scooter.ui.map.data.RateType
import kotlinx.coroutines.*
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit
import kotlin.time.minutes

class OrdersAdapter(
        var data: ArrayList<OrderWithStatusRate>,
        val context: Context,
        private val manipulatorDelegate: OrderManipulatorDelegate
) :
    RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {
    private val tickerJobs = hashMapOf<Long, Job>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder =
        OrdersViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_scooter_driving, parent, false)
        )

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        holder.constaraintViewParent.elevation = 0f
        holder.linnearLayoutScooterItemFinishButtons.visibility = View.GONE
        holder.constaraintViewParent.visibility = View.VISIBLE
        holder.linnearLayoutScooterItemBookingButtons.visibility = View.GONE
        holder.linnearLayoutScooterItemRentButtons.visibility = View.GONE
        holder.linnearLayoutScooterItemFirstBookButtons.visibility = View.GONE
        holder.constraintLayoutScooterItemPopupLock.visibility = View.GONE
        holder.pausedStateContainer.visibility = View.GONE

        val scooterPercentage = data[position].scooter.getBatteryPercentage()
        val scooterInfo = data[position].scooter.getScooterRideInfo()

        val spannable: Spannable = SpannableString("$scooterPercentage $scooterInfo")

        spannable.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            scooterPercentage.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(Color.GRAY),
            scooterPercentage.length,
            "$scooterPercentage $scooterInfo".length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        holder.textViewItemScooterId.text = "#${data[position].order.scooter}"
        holder.scooterPercent.text = scooterPercentage
        holder.scooterPercentTime.text = "~$scooterInfo"
        holder.scooterPercentDistance.text = data[position].scooter.getScooterPercentDistance()

        Log.w("OrdersAdapter", data[position].status.value)

        when (data[position].status) {



            OrderStatus.BOOKED -> {
                holder.linnearLayoutScooterItemBookingButtons.visibility = View.VISIBLE
                holder.textViewItemScooterStateLabel.setText(R.string.scooter_booked)

                holder.buttonScooterItemCancelBook.setOnClickListener {

                    manipulatorDelegate.cancelOrder(data[position].order.id)

                    tickerJobs[data[position].order.id]?.cancel()
                }

                holder.buttonScooterItemActivate.setOnClickListener {

                    data[position].status = OrderStatus.CHOOSE_RATE

                    (context as DrivingsActivity).runOnUiThread {
                        notifyDataSetChanged()
                        notifyItemChanged(position)
                    }
                }


//                tickerJobs[data[position].order.id] = GlobalScope.launch(Dispatchers.Main) {
//
//
//                    val first = data[position].order.startTime
//
//
//                    System.out.println("FIRST ZONE FORMATED "+data[position].order.formattedDateStartTime())
//
//                    System.out.println("STAMO "+data[position].order.startTime)
//
//                    val totalSeconds =   TimeUnit.valueOf(data[position].order.startTime).toSeconds(0)
//                    val tickSeconds = 1
//                    for (second in totalSeconds downTo tickSeconds) {
//                        val time = String.format("%02d:%02d",
//                                TimeUnit.SECONDS.toMinutes(second),
//                                second - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(second))
//                        )
//                        (context as DrivingsActivity).runOnUiThread {
//                            holder.textViewItemScooterStateValue.text = time
//                        }
//                        delay(1000)
//                    }
//                    System.out.println("FOR")
//                }

                tickerJobs[data[position].order.id] = GlobalScope.launch {
                    try {
                        val orderTime = data[position].order.parseStartTime()


                        while (true) {

                            if (orderTime != null) {
                                val time =
                                    LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
                                        .toEpochMilli() - orderTime



                                val rawMinutes = TimeUnit.MILLISECONDS.toMinutes(time)

                                val hours = rawMinutes / 60
                                val minutes = rawMinutes % 60
                                val seconds = time / 1000 - minutes * 60 - hours * 3600

                                (context as DrivingsActivity).runOnUiThread {
                                    holder.textViewItemScooterStateValue.text =
                                        String.format("%d:%02d:%02d", hours, minutes, seconds)
                                }
                            }

                            delay(1000)
                        }
                    } catch (e: Exception) {
                    }
                }
            }

            OrderStatus.CLOSED -> {

          
                tickerJobs[data[position].order.id]?.cancel()

                holder.pausedStateContainer.visibility = View.VISIBLE

                holder.buttonResumeScooter.setOnClickListener {
                    manipulatorDelegate.resumeOrder(data[position].order.id)
                }
                holder.textViewItemScooterMinutePricing.text = data[position].rate.pauseRate+"р"
                holder.buttonCloseOrder.setOnClickListener {
                    manipulatorDelegate.closeOrder(data[position].order.id)
                }

                holder.textViewItemScooterStateLabel.setText("ПАУЗА: ")
                holder.textViewItemScooterStateValue.text =
                        String.format("%.2f", data[position].order.cost).plus(" р")
            }

            OrderStatus.CHOOSE_RATE -> {
                holder.linnearLayoutScooterItemRentButtons.visibility = View.VISIBLE
                holder.textViewItemScooterStateLabel.setText(R.string.scooter_booked)

                holder.buttonScooterItemPerMinute.setOnClickListener {
                    manipulatorDelegate.setRateAndActivate(data[position].order.id, RateType.MINUTE)
                }

                holder.buttonScooterPerHour.setOnClickListener {
                    manipulatorDelegate.setRateAndActivate(data[position].order.id, RateType.HOUR)
                }

                tickerJobs[data[position].order.id] = GlobalScope.launch {
                    try {
                        val orderTime = data[position].order.parseStartTime()

                        while (true) {

                            if (orderTime != null) {
                                val time =
                                    LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
                                        .toEpochMilli() - orderTime
                                val rawMinutes = TimeUnit.MILLISECONDS.toMinutes(time)

                                val hours = rawMinutes / 60
                                val minutes = rawMinutes % 60
                                val seconds = time / 1000 - minutes * 60 - hours * 3600



                                (context as DrivingsActivity).runOnUiThread {
                                    holder.textViewItemScooterStateValue.text =
                                        String.format("%d:%02d:%02d", hours, minutes, seconds)
                                }
                            }
                            delay(1000)
                        }
                    } catch (e: Exception) {
                    }
                }
            }

            OrderStatus.ACTIVATED -> {
                tickerJobs[data[position].order.id]?.cancel()

               // holder.constraintLayoutScooterItemPopupLock.visibility = View.VISIBLE
                holder.linnearLayoutScooterItemFinishButtons.visibility = View.VISIBLE

                holder.buttonScooterItemPause.setOnClickListener {
                      manipulatorDelegate.pauseOrder(data[position].order.id)
                }

                holder.buttonScooterFinish.setOnClickListener {
                    manipulatorDelegate.closeOrder(data[position].order.id)
                }
                holder.constraintLayoutScooterItemPopupLock.setOnClickListener {
                    manipulatorDelegate.openLook(data[position].scooter.id)
                }



                holder.textViewItemScooterMinutePricing.text = data[position].rate.minute+"p"
                holder.textViewItemScooterStateLabel.setText(R.string.scooter_rented)
                holder.textViewItemScooterStateValue.text =
                    String.format("%.2f", data[position].order.cost).plus(" р")
            }




        }
    }

    override fun getItemCount(): Int = data.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        tickerJobs.values.forEach(Job::cancel)
    }

    inner class OrdersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.cardViewScooterItem)
        val constaraintViewParent: ConstraintLayout =
            itemView.findViewById(R.id.constraintLayoutItemScooterParent)
        val linnearLayoutScooterItemFinishButtons: LinearLayout =
            cardView.findViewById(R.id.linnearLayoutScooterItemFinishButtons)
        val linnearLayoutScooterItemBookingButtons: LinearLayout =
            cardView.findViewById(R.id.linnearLayoutScooterItemBookingButtons)


        val linnearLayoutScooterItemRentButtons: LinearLayout =
            cardView.findViewById(R.id.linnearLayoutScooterItemRentButtons)
        val linnearLayoutScooterItemFirstBookButtons: LinearLayout =
            cardView.findViewById(R.id.linnearLayoutScooterItemFirstBookButtons)
        val constraintLayoutScooterItemPopupLock: ConstraintLayout =
            cardView.findViewById(R.id.constraintLayoutScooterItemPopupLock)
        val textViewItemScooterId: TextView =
            cardView.findViewById(R.id.textViewItemScooterId)
        val scooterPercent: TextView =
            cardView.findViewById(R.id.scooterPercent)
        val textViewItemScooterMinutePricing: TextView =
                cardView.findViewById(R.id.textViewItemScooterMinutePricing)
        val scooterPercentTime: TextView =
                cardView.findViewById(R.id.scooterPercentTime)
        val scooterPercentDistance: TextView =
                cardView.findViewById(R.id.scooterPercentDistance)
        val textViewItemScooterStateLabel: TextView =
            cardView.findViewById(R.id.textViewItemScooterStateLabel)
        val buttonScooterItemCancelBook: Button =
            cardView.findViewById(R.id.buttonScooterItemCancelBook)
        val buttonScooterItemActivate: Button =
            cardView.findViewById(R.id.buttonScooterItemActivate)
        val textViewItemScooterStateValue: TextView =
            cardView.findViewById(R.id.textViewItemScooterStateValue)
        val buttonScooterItemPerMinute: Button =
            cardView.findViewById(R.id.buttonScooterItemPerMinute)
        val buttonScooterPerHour: Button = cardView.findViewById(R.id.buttonScooterPerHour)
        val buttonScooterItemPause: Button =
            cardView.findViewById(R.id.buttonScooterItemPause)
        val pausedStateContainer: LinearLayout = cardView.findViewById(R.id.pausedStateContainer)
        val buttonCloseOrder: Button = cardView.findViewById(R.id.buttonCloseOrder)
        val buttonScooterFinish: Button = cardView.findViewById(R.id.buttonScooterFinish)
        val buttonResumeScooter: Button = cardView.findViewById(R.id.buttonResumeScooter)
    }
}