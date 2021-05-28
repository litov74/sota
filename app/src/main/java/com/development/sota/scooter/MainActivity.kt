
package com.development.sota.scooter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.development.sota.scooter.net.LoginRetrofitProvider
import com.development.sota.scooter.ui.login.presentation.LoginActivity
import com.development.sota.scooter.ui.map.presentation.MapActivity
import com.development.sota.scooter.ui.tutorial.presentation.TutorialActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpAppCompatActivity

class MainActivity : MvpAppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCenter.start(
            application, getString(R.string.app_center_key),
            Analytics::class.java, Crashes::class.java
        )
        sharedPreferences = getSharedPreferences(SP_KEY_ACCOUNT, Context.MODE_PRIVATE)

        LoginRetrofitProvider.serviceDNS
            .getDNS()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {  },
                onNext = {
                    it.Answer.forEach{
                        if (it.data.contains("backend")) {
                            var dev = it.data.split("p=").get(1).split("t=").first().trim()
                            val prod = it.data.split("t=").get(1).trim()
                            if (BuildConfig.BUILD_TYPE.compareTo("debug") == 0) {
                                sharedPreferences.edit().putString("host", dev).apply()
                            } else if (BuildConfig.BUILD_TYPE.compareTo("relese") == 0) {
                                sharedPreferences.edit().putString("host", prod).apply()
                            }
                        }
                    }

                    val classActivity: Class<*> =
                        if (!sharedPreferences.getBoolean(SP_KEY_FIRST_INIT, false)) {
                            LoginActivity::class.java
                        } else if (!sharedPreferences.getBoolean(SP_KEY_WAS_TUTORIAL, false)) {
                            TutorialActivity::class.java
                        } else {
                            MapActivity::class.java
                        }
                    startActivity(Intent(this, classActivity))
                })


    }

    companion object {
        const val SP_KEY_ACCOUNT = "account"
        const val SP_KEY_FIRST_INIT = "firstInit"
        const val SP_KEY_WAS_TUTORIAL = "wasTutorial"
    }
}