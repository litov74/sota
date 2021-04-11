package com.development.sota.scooter.ui.profile.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import com.development.sota.scooter.MainActivity
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.ActivityProfileBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd


interface ProfileView : MvpView {
    @AddToEnd
    fun setProfileInfo(clientName: String, surname: String, phone: String, imgUrl: String?)

    @AddToEnd
    fun showError(msg: String)

    @AddToEnd
    fun setLoading(by: Boolean)

    @AddToEnd
    fun profileUpdate()
}

class ProfileActivity : MvpAppCompatActivity(), ProfileView {

    private val presenter by moxyPresenter { ProfilePresenter(this) }

    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)

        binding.imageButtonDrivingsListBack.setOnClickListener {
            onBackPressed()
        }
        binding.signOut.setOnClickListener {
            val prefs = getSharedPreferences(MainActivity.SP_KEY_ACCOUNT, Context.MODE_PRIVATE)
            prefs.edit(commit = true) {
                clear()
            }
            val intent = Intent(this@ProfileActivity, MainActivity::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
            )
            startActivity(intent)
        }

        binding.additionalProfileText.setOnClickListener {
            //TODO: need profile editing
            //val tmp: String = et_name.toString()
            //val tmp2: String = et_phone.toString()
            //var name: String = ""
            //var surname: String = ""

            //if(tmp.contains(" ")){
            //    name = tmp.split(" ").first().toString()
            //    surname = tmp.split(" ").last().toString()
            //    setProfileInfo(name, surname, tmp2, "")
            //    profileUpdate()
            //}else{
            //    setProfileInfo(tmp, "", tmp2, "")
            //    profileUpdate()
            //}

        }

        setContentView(binding.root)

        presenter.getProfileInfo()
    }

    override fun setProfileInfo(
        clientName: String,
        surname: String,
        phone: String,
        imgUrl: String?
    ) {
        binding.etName.setText(clientName)
        binding.etSurname.setText(surname)
        binding.etPhone.setText(phone)

        Picasso.get()
            .load("http://www.scooteradminpanel.ru/static$imgUrl")
            .placeholder(R.drawable.ic_insert_picture)
            .error(R.drawable.ic_insert_picture)
            .into(binding.photo)
    }

    override fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun setLoading(by: Boolean) {
        runOnUiThread {
            if (by) {
                binding.progressBarProfile.visibility = View.VISIBLE
            } else {
                binding.progressBarProfile.visibility = View.GONE
            }
        }
    }

    override fun profileUpdate() {
        Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show()
    }
}