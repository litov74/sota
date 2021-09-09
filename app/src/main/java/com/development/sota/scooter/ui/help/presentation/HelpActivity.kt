package com.development.sota.scooter.ui.help.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.BuildConfig
import com.development.sota.scooter.MainActivity
import com.development.sota.scooter.common.Constants.WHATSAPP_PHONE_NUMBER
import com.development.sota.scooter.databinding.ActivityHelpBinding
import com.development.sota.scooter.net.LoginRetrofitProvider
import com.development.sota.scooter.ui.login.presentation.LoginActivity
import com.development.sota.scooter.ui.map.presentation.MapActivity
import com.development.sota.scooter.ui.tutorial.presentation.TutorialActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

interface HelpView : MvpView

interface HelpActivityView

class HelpActivity : MvpAppCompatActivity(), HelpView, HelpActivityView {

    private val presenter by moxyPresenter { HelpPresenter(this) }
    lateinit var sharedPreferences: SharedPreferences
    private var _binding: ActivityHelpBinding? = null
    private val binding get() = _binding!!
    var adapter: HelpAdapter? = null
    var telegramLink: String = ""
    var phoneNumber: String = ""

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        _binding = ActivityHelpBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences(MainActivity.SP_KEY_ACCOUNT, Context.MODE_PRIVATE)

        setContentView(binding.root)
        binding.helpImageButtonBack.setOnClickListener {
            onBackPressed()
        }

        LoginRetrofitProvider.service
            .getSupportContacts()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {  },
                onNext = {

                    sharedPreferences.edit().putString("telegramLink", it.asJsonObject.get("tg").toString()).apply()
                    sharedPreferences.edit().putString("phoneNumberSupport", it.asJsonObject.get("phone").toString()).apply()

                    telegramLink = sharedPreferences.getString("telegramLink", "").toString()
                    phoneNumber = sharedPreferences.getString("phoneNumberSupport", "").toString()


                    val titles = arrayListOf("Telegram", "Email")
                    val recyclerView: RecyclerView = binding.helpList
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    adapter = HelpAdapter(this, titles,telegramLink)

                    recyclerView.adapter = adapter
//                    val dividerItemDecoration = DividerItemDecoration(
//                        recyclerView.context,
//                        LinearLayout.VERTICAL
//                    )
//                    recyclerView.addItemDecoration(dividerItemDecoration)

                    binding.phoneCall.setOnClickListener {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
                        startActivity(intent)
                    }
                })



    }
}