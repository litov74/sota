package com.development.sota.scooter.ui.purse.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.ActivityPurseBinding
import com.development.sota.scooter.ui.profile.domain.entities.ClientData
import com.development.sota.scooter.ui.profile.domain.entities.ClientDataItem
import com.development.sota.scooter.ui.purse.presentation.fragments.cards.CardsFragment
import com.development.sota.scooter.ui.purse.presentation.fragments.transactions.TransactionsFragment
import com.development.sota.scooter.ui.purse.presentation.fragments.upbalance.UpBalanceFragment
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter

interface WalletView : MvpView
interface WalletActivityView

class WalletActivity : MvpAppCompatActivity(), WalletView, WalletActivityView {

    private val presenter by moxyPresenter { PursePresenter(this) }

    private var _binding: ActivityPurseBinding? = null
    private val binding get() = _binding!!

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        _binding = ActivityPurseBinding.inflate(layoutInflater)

        binding.imageButtonDrivingsListBack.setOnClickListener {
            onBackPressed()
        }

        var segmentId = 0
        val fm = supportFragmentManager
        fm.beginTransaction().replace(R.id.host, UpBalanceFragment()).commit()

        binding.btnOpenUpBalance.setOnClickListener {
            if (segmentId != 0) {
                unCheck(binding.btnOpenCards)
                unCheck(binding.btnOpenTransactions)
                check(binding.btnOpenUpBalance)

                segmentId = 0

                fm.beginTransaction().replace(R.id.host, UpBalanceFragment()).commit()
            }
        }

        binding.btnOpenCards.setOnClickListener {
            if (segmentId != 1) {
                unCheck(binding.btnOpenUpBalance)
                unCheck(binding.btnOpenTransactions)
                check(binding.btnOpenCards)

                segmentId = 1
                fm.beginTransaction().replace(R.id.host, CardsFragment()).commit()

            }
        }

        binding.btnOpenTransactions.setOnClickListener {
            if (segmentId != 2) {
                unCheck(binding.btnOpenUpBalance)
                unCheck(binding.btnOpenCards)
                check(binding.btnOpenTransactions)

                segmentId = 2
                fm.beginTransaction().replace(R.id.host, TransactionsFragment()).commit()
            }
        }



        setContentView(binding.root)

    }

    private fun unCheck(btn: Button){
        btn.background =
            ContextCompat.getDrawable(this, R.drawable.ic_gray_segment_corner)
        btn.elevation = 0f
    }

    private fun check(btn: Button){
        btn.background =
            ContextCompat.getDrawable(this, R.drawable.ic_white_corner)
        btn.elevation = 4f
    }

    fun showToastWarning() {
        Toast.makeText(this, getString(R.string.error_api), Toast.LENGTH_SHORT).show()
    }
}