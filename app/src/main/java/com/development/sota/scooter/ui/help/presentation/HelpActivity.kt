package com.development.sota.scooter.ui.help.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.common.Constants.WHATSAPP_PHONE_NUMBER
import com.development.sota.scooter.databinding.ActivityHelpBinding
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter

interface HelpView : MvpView

interface HelpActivityView

class HelpActivity : MvpAppCompatActivity(), HelpView, HelpActivityView {

    private val presenter by moxyPresenter { HelpPresenter(this) }

    private var _binding: ActivityHelpBinding? = null
    private val binding get() = _binding!!
    var adapter: HelpAdapter? = null

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        _binding = ActivityHelpBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.helpImageButtonBack.setOnClickListener {
            onBackPressed()
        }

        val titles = arrayListOf("Telegram", "WhatsApp")
        val recyclerView: RecyclerView = binding.helpList
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HelpAdapter(this, titles)

        recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayout.VERTICAL
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        binding.phoneCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", WHATSAPP_PHONE_NUMBER, null))
            startActivity(intent)
        }
    }
}