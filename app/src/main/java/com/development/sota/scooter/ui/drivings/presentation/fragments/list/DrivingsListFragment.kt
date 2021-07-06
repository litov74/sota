package com.development.sota.scooter.ui.drivings.presentation.fragments.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.FragmentDrivingsListBinding
import com.development.sota.scooter.ui.drivings.domain.entities.OrderWithStatusRate
import com.development.sota.scooter.ui.drivings.presentation.DrivingsActivityView
import com.development.sota.scooter.ui.drivings.presentation.DrivingsListFragmentType
import com.development.sota.scooter.ui.help.presentation.HelpActivity
import com.development.sota.scooter.ui.map.data.RateType
import com.development.sota.scooter.ui.tutorial.presentation.TutorialFinishActivity
import com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd


interface DrivingsListView : MvpView {
    @AddToEnd
    fun showToast(message: String)

    @AddToEnd
    fun setLoading(by: Boolean)

    @AddToEnd
    fun initViewPager2(data: Pair<ArrayList<OrderWithStatusRate>, ArrayList<OrderWithStatusRate>>)

    @AddToEnd
    fun clearViewPage()

    @AddToEnd
    fun pauseScooterSuccess()

    @AddToEnd
    fun openLookScooterSuccess()

    @AddToEnd
    fun openLookScooterError()

    @AddToEnd
    fun pauseScooterError()

    @AddToEnd
    fun resumeScooterSuccess()

    @AddToEnd
    fun resumeScooterError()

    @AddToEnd
    fun openFinishTutorial()
}

interface OrderManipulatorDelegate {
    fun cancelOrder(id: Long)

    fun activateOrder(id: Long)

    fun resumeOrder(id: Long)

    fun pauseOrder(id: Long)

    fun openLook(id: Long)

    fun setRateAndActivate(id: Long, type: RateType)

    fun closeOrder(id: Long)
}

class DrivingsListFragment(val drivingsView: DrivingsActivityView) : MvpAppCompatFragment(),
    DrivingsListView, OrderManipulatorDelegate {
    private val presenter by moxyPresenter {
        DrivingsListPresenter(
            context ?: activity?.applicationContext!!
        )
    }

    private var _binding: FragmentDrivingsListBinding? = null
    private val binding get() = _binding!!
    private var adapter: DrivingsListViewPager2Adapter? = null

    private var segmentId = 0
    private lateinit var progressDialog: MaterialDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrivingsListBinding.inflate(inflater, container, false)

        binding.imageButtonDrivingsListBack.setOnClickListener {
            drivingsView.onBackPressedByType(DrivingsListFragmentType.LIST)
        }

        binding.viewPager2DrivingsList.isUserInputEnabled = false

        binding.buttonDrivingsListActive.setOnClickListener {
            if (segmentId == 1) {
                binding.buttonDrivingsListActive.background =
                    ContextCompat.getDrawable(context!!, R.drawable.ic_white_corner)
                binding.buttonDrivingsListActive.elevation = 4f

                binding.buttonDrivingsListHistory.background =
                    ContextCompat.getDrawable(context!!, R.drawable.ic_gray_segment_corner)
                binding.buttonDrivingsListHistory.elevation = 0f

                segmentId = 0
                binding.viewPager2DrivingsList.currentItem = segmentId
            }
        }

        binding.buttonDrivingsListHistory.setOnClickListener {
            if (segmentId == 0) {
                binding.buttonDrivingsListHistory.background =
                    ContextCompat.getDrawable(context!!, R.drawable.ic_white_corner)
                binding.buttonDrivingsListHistory.elevation = 4f

                binding.buttonDrivingsListActive.background =
                    ContextCompat.getDrawable(context!!, R.drawable.ic_gray_segment_corner)
                binding.buttonDrivingsListActive.elevation = 0f

                segmentId = 1
                binding.viewPager2DrivingsList.currentItem = segmentId
            }
        }

        return binding.root
    }

    override fun showToast(message: String) {
        try {
            activity?.runOnUiThread {
                Toast.makeText(context!!, message, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        presenter.resumeView()
    }

    override fun setLoading(by: Boolean) {
        activity?.runOnUiThread {
            if (by) {
                binding.progressBarDrivingsList.visibility = View.VISIBLE
                binding.linnearLayoutSegmentContol.isEnabled = false
                binding.viewPager2DrivingsList.isEnabled = false

                progressDialog = MaterialDialog.Builder(requireContext())
                    .title("Уведомление")
                    .content("Пожалуйста подождите")
                    .cancelable(false)
                    .progress(true, 0)
                    .show()

            } else {
                progressDialog.cancel()

                binding.progressBarDrivingsList.visibility = View.GONE
                binding.linnearLayoutSegmentContol.isEnabled = true
                binding.viewPager2DrivingsList.isEnabled = true
            }
        }
    }

    override fun initViewPager2(data: Pair<ArrayList<OrderWithStatusRate>, ArrayList<OrderWithStatusRate>>) {
        activity?.runOnUiThread {
            adapter = DrivingsListViewPager2Adapter(activity!!, data, this)
            binding.viewPager2DrivingsList.adapter = adapter
            binding.viewPager2DrivingsList.requestTransform()
        }
    }

    override fun clearViewPage() {
        activity?.runOnUiThread {
            binding.viewPager2DrivingsList.setSaveFromParentEnabled(false);
        }
    }

    override fun pauseScooterSuccess() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.scooter_pause_success))
        builder.setPositiveButton("Ок") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    override fun openLookScooterSuccess() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.scooter_open_look_success))
        builder.setPositiveButton("Ок") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    override fun openLookScooterError() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.notification))
        builder.setMessage("Не удалось открыть замок")

        builder.setPositiveButton("Повторить") { dialog, which ->
            presenter.repeatOpenLook()
            dialog.dismiss()
        }

        builder.setNegativeButton("Поддержка") { dialog, which ->
            activity?.let{
                val intent = Intent (it, HelpActivity::class.java)
                it.startActivity(intent)
            }
            dialog.dismiss()
        }

        builder.show()
    }

    override fun pauseScooterError() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.notification))
        builder.setMessage("Не удалось получить ответ от самоката")


        builder.setPositiveButton("Повторить") { dialog, which ->
            presenter.repeatScooterPause()
            dialog.dismiss()
        }

        builder.setNegativeButton("Поддержка") { dialog, which ->
            activity?.let{
                val intent = Intent (it, HelpActivity::class.java)
                it.startActivity(intent)
            }
            dialog.dismiss()
        }

        builder.show()
    }

    override fun resumeScooterSuccess() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Самокат готов к поездке")
        builder.setPositiveButton("Ок") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    override fun resumeScooterError() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.notification))
        builder.setMessage("Не удалось получить ответ от самоката")


        builder.setPositiveButton("Повторить") { dialog, which ->
            presenter.repeatScooterResume()
            dialog.dismiss()
        }

        builder.setNegativeButton("Поддержка") { dialog, which ->
            activity?.let{
                val intent = Intent (it, HelpActivity::class.java)
                it.startActivity(intent)
            }
            dialog.dismiss()
        }

        builder.show()
    }

    override fun openFinishTutorial() {
        runOnUiThread {
            requireActivity().startActivity(
                Intent(
                    requireContext(),
                    TutorialFinishActivity::class.java
                )
            )
        }
    }

    override fun cancelOrder(id: Long) {
        presenter.cancelOrder(id)
    }

    override fun activateOrder(id: Long) {
        presenter.activateOrder(id)
    }

    override fun resumeOrder(id: Long) {
        presenter.resumeOrder(id)
    }

    override fun pauseOrder(id: Long) {
        presenter.pauseOrder(id)
    }

    override fun openLook(id: Long) {
        presenter.openLook(id)
    }

    override fun setRateAndActivate(id: Long, type: RateType) {
        presenter.setRateAndActivate(id, type)
    }

    override fun closeOrder(id: Long) {
        presenter.closeOrder(id)
    }

    override fun onDestroy() {
        presenter.onDestroyCalled()
        super.onDestroy()
    }
}
