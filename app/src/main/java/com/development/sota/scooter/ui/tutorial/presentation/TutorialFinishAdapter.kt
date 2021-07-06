package com.development.sota.scooter.ui.tutorial.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.R
import kotlinx.android.synthetic.main.fragment_tutorial.view.*

class TutorialFinishAdapter : RecyclerView.Adapter<TutorialFinishAdapter.TutorialViewHolder>() {
    private val data: ArrayList<Triple<Int, Int, Int>> = arrayListOf(
        Triple(
            R.string.finish_tutorial_title_1,
            R.string.finish_tutorial_body_1,
            R.drawable.ic_finish_scooter
        ),

    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorialViewHolder =
        TutorialViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_tutorial, parent, false)
        )

    override fun onBindViewHolder(holder: TutorialViewHolder, position: Int) = holder.itemView.run {
        textViewTutorialLabel.text = context.getString(data[position].first)
        textViewTutorialBody.text = context.getString(data[position].second)
        imageViewTutorial.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                data[position].third,
                null
            )
        )
    }

    override fun getItemCount(): Int = data.size

    inner class TutorialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}