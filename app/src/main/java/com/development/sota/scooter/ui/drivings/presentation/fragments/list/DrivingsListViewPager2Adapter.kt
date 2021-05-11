package com.development.sota.scooter.ui.drivings.presentation.fragments.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.R
import com.development.sota.scooter.ui.drivings.domain.entities.OrderWithStatus
import com.development.sota.scooter.ui.drivings.domain.entities.OrderWithStatusRate


class DrivingsListViewPager2Adapter(
        private val context: Context,
        private val data: Pair<ArrayList<OrderWithStatusRate>, ArrayList<OrderWithStatusRate>>,
        private val manipulatorDelegate: OrderManipulatorDelegate
) : RecyclerView.Adapter<DrivingsListViewPager2Adapter.DrivingsListViewPager2ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DrivingsListViewPager2ViewHolder =
        DrivingsListViewPager2ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_drivings_list, parent, false)
        )

    override fun onBindViewHolder(holder: DrivingsListViewPager2ViewHolder, position: Int) =
        holder.itemView.run {
            holder.recyclerViewItemDrivingsList.layoutManager = LinearLayoutManager(context)
            holder.recyclerViewItemDrivingsList.adapter = if (position == 0) OrdersAdapter(
                data.first,
                context,
                manipulatorDelegate
            ) else FinishedOrderAdapter(data.second, context)
        }

    override fun getItemCount(): Int = 2

    inner class DrivingsListViewPager2ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val recyclerViewItemDrivingsList: RecyclerView =
            itemView.findViewById(R.id.recyclerViewItemDrivingsList)
    }

}
