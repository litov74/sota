package com.development.sota.scooter.ui.purse.presentation.fragments.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.FragmentTransactionsBinding
import com.development.sota.scooter.ui.purse.domain.entities.TransactionModel
import com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd

interface TransactionsView : MvpView{
    @AddToEnd
    fun showToast(text: String)

    @AddToEnd
    fun setLoading(by: Boolean)

    @AddToEnd
    fun showTransactions(transactions: List<TransactionModel>)

}

class TransactionsActivity: MvpAppCompatFragment(R.layout.fragment_transactions), TransactionsView {
    private val presenter by moxyPresenter {
        TransactionsPresenter(
            context ?: requireActivity().applicationContext
        )
    }
    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onResume() {
        super.onResume()
        presenter.resumeView()
    }

    override fun onDestroy() {
        presenter.onDestroyCalled()
        super.onDestroy()
    }

    override fun showToast(text: String) {
        runOnUiThread {
        }
    }

    override fun setLoading(by: Boolean) {
        runOnUiThread {
        }
    }

    override fun showTransactions(transactions: List<TransactionModel>) {
        runOnUiThread {
                binding.content.apply {
                    binding.content.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.content.adapter = TransactionAdapter(transactions)
                }
        }
    }
}