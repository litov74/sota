package com.development.sota.scooter.ui.purse.presentation.fragments.upbalance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.ui.purse.domain.entities.UpBalancePackageModel


class UpBalanceAdapter(private val list: List<UpBalancePackageModel>, private val upBalanceManipulatorDelegate: UpBalanceManipulatorDelegate)
    : RecyclerView.Adapter<UpBalanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpBalanceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UpBalanceViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: UpBalanceViewHolder, position: Int) {
        val upBalanceModel: UpBalancePackageModel = list[position]
        holder.bind(upBalanceModel, upBalanceManipulatorDelegate)
    }

    override fun getItemCount(): Int = list.size
}