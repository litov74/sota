package com.development.sota.scooter.ui.purse.presentation.fragments.cards

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.FragmentCardsBinding
import com.development.sota.scooter.ui.purse.domain.entities.UserCardModel
import com.development.sota.scooter.ui.purse.presentation.AddCardActivity
import com.development.sota.scooter.ui.purse.presentation.WalletActivity
import com.development.sota.scooter.ui.purse.presentation.fragments.transactions.TransactionAdapter
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
    fun showCards(userCardModels: List<UserCardModel>)

    @AddToEnd
    fun showProgress(boolean: Boolean)
}

interface UserCardAction  {

    fun setMain(userCardModel: UserCardModel)

    fun selectRemove(userCardModel: UserCardModel)

}

class CardListFragment : MvpAppCompatFragment(R.layout.fragment_cards), ICardList, UserCardAction {
    private val presenter by moxyPresenter {
        CardListPresenter(
            context ?: requireActivity().applicationContext
        )
    }
    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!
    private var cardListAdapter: UserCardAdapter? = null
    private var editMode: Boolean = false

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

    override fun showCards(userCardModels: List<UserCardModel>) {
        runOnUiThread {
                (activity as WalletActivity).setCardCount(userCardModels.size)
                cardListAdapter = UserCardAdapter(userCardModels,editMode,this)
                binding.cardListContainer.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.cardListContainer.adapter = cardListAdapter
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

    override fun setMain(userCardModel: UserCardModel) {
        val builder = AlertDialog.Builder(requireContext())
        builder
                .setTitle("Внимание")
                .setMessage("Сделать карту ••• "+userCardModel.last_four+" главной?")
                .setPositiveButton("Да") {
                    dialog, id ->  dialog.cancel()
                    presenter.setMain(userCardModel)
                }
                .setNegativeButton("Нет") {
                    dialog, id ->  dialog.cancel()
                }

        builder.create()
        builder.show()
    }

     fun changeEditMode() {
        editMode = editMode != true
        cardListAdapter?.editMode = editMode
        cardListAdapter?.notifyDataSetChanged()
    }

    fun cancelEditMode() {
        editMode = false
        cardListAdapter?.editMode = editMode
        cardListAdapter?.notifyDataSetChanged()
    }

    override fun selectRemove(userCardModel: UserCardModel) {
        val builder = AlertDialog.Builder(requireContext())
        builder
                .setTitle("Внимание")
                .setMessage("Удалить карту ••• "+userCardModel.last_four+"?")
                .setPositiveButton("Да") {
                    dialog, id ->  dialog.cancel()
                    presenter.removeCard(userCardModel)
                }
                .setNegativeButton("Нет") {
                    dialog, id ->  dialog.cancel()
                }
        builder.create()
        builder.show()
    }

}