package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.FragmentCardsBinding
import com.development.sota.scooter.ui.purse.domain.entities.Card
import com.development.sota.scooter.ui.purse.presentation.AddCardActivity
import com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd

interface ICardList : MvpView{
    @AddToEnd
    fun showToast(text: String)

    @AddToEnd
    fun showUserCards(last_four: String, main: String)

    @AddToEnd
    fun showCards(cards: List<Card>)

    @AddToEnd
    fun showProgress(boolean: Boolean)
}



class CardListFragment : MvpAppCompatFragment(R.layout.fragment_cards), ICardList{
    private val presenter by moxyPresenter {
        CardListPresenter(
            context ?: requireActivity().applicationContext
        )
    }
    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardsBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    fun initView() {
        binding.addCardContainer.setOnClickListener {
            activity?.let {
                val intent = Intent(it, AddCardActivity::class.java)
                it.startActivity(intent)
            }
        }
    }



    override fun onDestroy() {
        presenter.onDestroyCalled()
        super.onDestroy()
    }

    override fun showToast(text: String) {
        runOnUiThread {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.resumeView()
    }

    override fun showUserCards(last_four: String, main: String) {
        runOnUiThread {

        }
    }

    override fun showCards(cards: List<Card>) {
        runOnUiThread {

        }
    }

    override fun showProgress(boolean: Boolean) {
        runOnUiThread {
            if (boolean) {
                binding.progressBarCardsList.visibility = View.VISIBLE
                binding.contentContainer.visibility = View.GONE
            } else {
                binding.progressBarCardsList.visibility = View.GONE
                binding.contentContainer.visibility = View.VISIBLE
            }
        }
    }


}