package com.development.sota.scooter.ui.promo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.development.sota.scooter.R
import kotlinx.android.synthetic.main.activity_invite_friends.*

class InviteFriendsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_friends)

        invite_friends_image_button_back.setOnClickListener {
            onBackPressed()
        }

        share_with_friends.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }
    }
}