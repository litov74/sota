package com.development.sota.scooter.ui.tutorial.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.R
import kotlinx.android.synthetic.main.fragment_tutorial.view.*

class TutorialAdapter : RecyclerView.Adapter<TutorialAdapter.TutorialViewHolder>() {
    private val data: ArrayList<Triple<Int, Int, Int>> = arrayListOf(
        Triple(
            R.string.login_tutorial_label_1,
            R.string.login_tutorial_body_1,
            R.drawable.ic_frame_1
        ),
        Triple(
            R.string.login_tutorial_label_2,
            R.string.login_tutorial_body_2,
            R.drawable.ic_frame_2
        ),
        Triple(
            R.string.login_tutorial_label_3,
            R.string.login_tutorial_body_3,
            R.drawable.ic_frame_3
        ),
        Triple(
            R.string.login_tutorial_label_4,
            R.string.login_tutorial_body_4,
            R.drawable.ic_frame_4
        ),
        Triple(
            R.string.login_tutorial_label_5,
            R.string.login_tutorial_body_5,
            R.drawable.ic_frame_5
        ),
        Triple(
            R.string.login_tutorial_label_6,
            R.string.login_tutorial_body_6,
            R.drawable.ic_frame_6
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