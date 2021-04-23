package com.development.sota.scooter.ui.purse.presentation.fragments.upbalance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.FragmentUpBalanceBinding
import com.development.sota.scooter.ui.map.data.RateType
import com.development.sota.scooter.ui.purse.domain.entities.UpBalancePackageModel
import com.development.sota.scooter.ui.purse.presentation.WalletActivity
import com.development.sota.scooter.ui.purse.presentation.fragments.cards.CardListPresenter
import com.development.sota.scooter.ui.purse.presentation.fragments.transactions.TransactionAdapter
import com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd


interface UpBalanceView : MvpView {

    @AddToEnd
    fun showProgress(by: Boolean)

    @AddToEnd
    fun setListPackages(list: List<UpBalancePackageModel>)

    @AddToEnd
    fun updateUserBalance()
}

interface UpBalanceManipulatorDelegate {
    fun selectPackage(id: Long)

}

class UpBalanceActivity: MvpAppCompatFragment(R.layout.fragment_up_balance), UpBalanceView, UpBalanceManipulatorDelegate {
    private val presenter by moxyPresenter {
        UpBalancePresenter(
            context ?: requireActivity().applicationContext
        )
    }
    private var _binding: FragmentUpBalanceBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpBalanceBinding.inflate(inflater, container, false)
        binding.listPacks.layoutManager = GridLayoutManager(context, 2)
        return binding.root
    }

    override fun onDestroy() {
        presenter.onDestroyCalled()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        presenter.resumeView()
    }

    override fun showProgress(by: Boolean) {
        runOnUiThread {
            if (by) {
                binding.listPacks.visibility = View.GONE
                binding.progressLoading.visibility = View.VISIBLE
            } else {
                binding.listPacks.visibility = View.VISIBLE
                binding.progressLoading.visibility = View.GONE
            }

        }
    }

    override fun setListPackages(list: List<UpBalancePackageModel>) {
        runOnUiThread {
                val grid = GridLayoutManager(context, 2)
                binding.listPacks.layoutManager = grid
                binding.listPacks.adapter = UpBalanceAdapter(list, this)
        }
    }

    override fun updateUserBalance() {
        (context as WalletActivity).needUpdateBalance()
    }

    override fun selectPackage(id: Long) {
        presenter.upBalancePackage(id)
    }

}