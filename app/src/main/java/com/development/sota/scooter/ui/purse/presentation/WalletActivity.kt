package com.development.sota.scooter.ui.purse.presentation

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.ActivityPurseBinding
import com.development.sota.scooter.ui.purse.presentation.fragments.cards.CardListFragment

import com.development.sota.scooter.ui.purse.presentation.fragments.transactions.TransactionsActivity
import com.development.sota.scooter.ui.purse.presentation.fragments.upbalance.UpBalanceActivity
import kotlinx.android.synthetic.main.activity_purse.*
import kotlinx.android.synthetic.main.fragment_cards.*
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd


interface WalletView: MvpView {
    @AddToEnd
    fun showToast(text: String)

    @AddToEnd
    fun setLoading(by: Boolean)

    @AddToEnd
    fun setUserBalance(value: String)
}

interface WalletActivityView {

    fun needUpdateBalance()

    fun setCardCount(count: Int)
}

class WalletActivity : MvpAppCompatActivity(), WalletView, WalletActivityView{

    private val presenter by moxyPresenter { WalletPresenter(this) }

    private var _binding: ActivityPurseBinding? = null
    private val binding get() = _binding!!
    private var cardListFragment: CardListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPurseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageButtonDrivingsListBack.setOnClickListener{
            onBackPressed()
        }


        var key: Int = 0
        val fm = supportFragmentManager
        fm.beginTransaction().add(binding.host.id, UpBalanceActivity()).commit()
        binding.btnOpenUpBalance.setOnClickListener{
            if(key != 0){
                binding.editCardsContainer.visibility = View.GONE
                unCheck(binding.btnOpenCards)
                unCheck(binding.btnOpenTransactions)
                check(binding.btnOpenUpBalance)
                key = 0
                fm.beginTransaction().replace(binding.host.id, UpBalanceActivity()).commit()
            }
        }
        binding.btnOpenCards.setOnClickListener{
            if(key != 1){
                binding.editCardsContainer.visibility = View.GONE
                check(binding.btnOpenCards)
                unCheck(binding.btnOpenTransactions)
                unCheck(binding.btnOpenUpBalance)
                key = 1
                cardListFragment = CardListFragment()
                fm.beginTransaction().replace(binding.host.id, cardListFragment!!).commit()
            }
        }
        binding.btnOpenTransactions.setOnClickListener{
            if(key != 2){
                binding.editCardsContainer.visibility = View.GONE
                unCheck(binding.btnOpenCards)
                check(binding.btnOpenTransactions)
                unCheck(binding.btnOpenUpBalance)
                key = 2
                fm.beginTransaction().replace(binding.host.id, TransactionsActivity()).commit()
            }
        }
//        binding.addCardTextView.setOnClickListener {
//            fm.beginTransaction().replace(binding.host.id, AddCard()).commit()
//            addCardTextView.visibility = View.GONE
//        }


        binding.editCardsContainer.setOnClickListener { cardListFragment?.changeEditMode() }
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

    override fun setUserBalance(value: String) {
        (binding.balance as TextView).text = "$value ₽ "

    }

    override fun showToast(text: String) {
        runOnUiThread {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun setLoading(by: Boolean) {
        runOnUiThread {
          //  binding.progressBarDrivingsList.visibility = if (by) View.VISIBLE else View.GONE
        }
    }

    override fun needUpdateBalance() {
        presenter.updateUserBalance()
    }

    override fun setCardCount(count: Int) {
        if (count > 1) {
            binding.editCardsContainer.visibility = View.VISIBLE
        } else {
            binding.editCardsContainer.visibility = View.GONE
            cardListFragment?.cancelEditMode()
        }
    }
}
