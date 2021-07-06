package com.development.sota.scooter.ui.purse.presentation.fragments.upbalance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.FragmentUpBalanceBinding
import com.development.sota.scooter.ui.purse.domain.entities.UpBalancePackageModel
import com.development.sota.scooter.ui.purse.presentation.WalletActivity
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

    @AddToEnd
    fun showToast(message: String)

    @AddToEnd
    fun showAlertPayment(cost: String)

    @AddToEnd
    fun showUpBalanceOk(income: String)
}

interface UpBalanceManipulatorDelegate {
    fun selectPackage(model: UpBalancePackageModel)

}

class UpBalanceActivity: MvpAppCompatFragment(R.layout.fragment_up_balance), UpBalanceView, UpBalanceManipulatorDelegate {
    private val presenter by moxyPresenter {
        UpBalancePresenter(
            context ?: requireActivity().applicationContext
        )
    }
    private var _binding: FragmentUpBalanceBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: MaterialDialog

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
                binding.listPacks.adapter = UpBalanceAdapter(list.sortedBy { it.cost }, this)
        }
    }

    override fun updateUserBalance() {
        (context as WalletActivity).needUpdateBalance()
    }

    override fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showAlertPayment(cost: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_attention)
            .setMessage("Подтвердите покупку пакета за $cost руб и на старт!")
            .setPositiveButton("Подтверждаю") {
                    dialog, id ->  dialog.cancel()
                    presenter.confirmUpBalance()
            }
            .setNegativeButton("Отмена") {
                    dialog, id ->  dialog.cancel()
            }
            .create()
            .show()
    }

    override fun showUpBalanceOk(income: String) {
        requireActivity().runOnUiThread({
            AlertDialog.Builder(requireContext())
                .setTitle("Уведомление")
                .setMessage("Баланс пополнен на $income руб ")
                .setPositiveButton("Ок") {
                        dialog, id ->  dialog.cancel()
                }
                .create()
                .show()
        })

    }

    override fun selectPackage(model: UpBalancePackageModel) {
        presenter.upBalancePackage(model)
    }

}