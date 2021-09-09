package com.development.sota.scooter.ui.login.presentation.fragments.input

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.widget.doAfterTextChanged
import com.development.sota.scooter.R
import com.development.sota.scooter.databinding.FragmentLoginInputBinding
import com.development.sota.scooter.ui.login.presentation.LoginActivityView
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher


interface LoginInputView : MvpView {
    @AddToEnd
    fun changeFlag(drawable: Drawable)

    @AddToEnd
    fun lightThePhoneView(state: LoginInputPhoneState)

    @AddToEnd
    fun lightTheNameView(state: LoginInputPhoneState)

    @AddToEnd
    fun changeButtonRequestCodeState(enabled: Boolean, textId: Int)

    @AddToEnd
    fun setTheUserAgreementSwitchState(state: Boolean)

    @AddToEnd
    fun startUserAgreementFragment()

}

class LoginInputFragment(private val loginActivityView: LoginActivityView) : MvpAppCompatFragment(),
    LoginInputView {
    private val presenter by moxyPresenter {
        LoginInputPresenter(
            context ?: activity!!.applicationContext, loginActivityView
        )
    }

    private var _binding: FragmentLoginInputBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginInputBinding.inflate(inflater, container, false)

//        binding.loginPhonePinViewCode.doAfterTextChanged {
//            if (it == null || it.isEmpty()) {
//                binding.loginPhonePinViewCode.setText("+")
//            }
//
//            if (it!!.count() > 4) {
//                binding.loginPhonePinViewCode.text!!.removeRange(
//                    4,
//                    binding.loginPhonePinViewCode.text!!.count() - 1
//                )
//
//                activity!!.runOnUiThread {
//                    binding.loginPhonePinViewCodeFirstThreeNumbers.requestFocus()
//                }
//            }
//
//            presenter.onPhoneCodeChanged(getCurrentNumber())
//        }

        val mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
        val watcher: FormatWatcher = MaskFormatWatcher(mask)
        watcher.installOn(binding.mobilePhone)

        binding.mobilePhone.doAfterTextChanged({
            var phone = it.toString().replace("+", "").replace("(","").replace(")","").replace("-","").replace(" ","")
            presenter.setUserPhoneNumber(phone.trim());
        })
//        binding.loginPhonePinViewCodeFirstThreeNumbers.doAfterTextChanged {
//            if (it!!.count() == 3) {
//                activity!!.runOnUiThread {
//                    binding.loginPhonePinViewCodeSecondThreeNumbers.requestFocus()
//                }
//            }
//
//            presenter.onPhoneCodeChanged(getCurrentNumber())
//        }
//        binding.loginPhonePinViewCodeSecondThreeNumbers.doAfterTextChanged {
//            if (it!!.count() == 3) {
//                activity!!.runOnUiThread {
//                    binding.loginPhonePinViewCodeFirstTwoNumbers.requestFocus()
//                }
//            }
//
//            presenter.onPhoneCodeChanged(getCurrentNumber())
//        }
//        binding.loginPhonePinViewCodeFirstTwoNumbers.doAfterTextChanged {
//            if (it!!.count() == 2) {
//                activity!!.runOnUiThread {
//                    binding.loginPhonePinViewCodeSecondTwoNumbers.requestFocus()
//                }
//            }
//
//            presenter.onPhoneCodeChanged(getCurrentNumber())
//        }
//        binding.loginPhonePinViewCodeSecondTwoNumbers.doAfterTextChanged {
//            if (it!!.count() == 2) {
//                activity!!.runOnUiThread {
//                    binding.loginNamePinInput.requestFocus()
//                }
//            }
//
//            presenter.onPhoneCodeChanged(getCurrentNumber())
//        }
        binding.loginNamePinInput.doAfterTextChanged {
            presenter.onNameChanged(it?.toString() ?: "")
        }

        binding.switchLoginAgreement.setOnCheckedChangeListener { _, b ->
            presenter.onConfirmAgreementSwitchStateChanged(
                b
            )
        }
        binding.viewLoginClickableUserAgreement.setOnClickListener { presenter.userAgreementFragmentRequested() }
        binding.buttonLoginRequestCode.setOnClickListener { presenter.onRequestCodeButtonClicked() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun changeFlag(drawable: Drawable) {
        activity?.runOnUiThread {
         //   binding.imageViewLoginInputFlag.setImageDrawable(drawable)
        }
    }

    override fun lightThePhoneView(state: LoginInputPhoneState) {
        activity?.runOnUiThread {
//            binding.loginPhonePinViewCode.background.mutate()
//                .setTint(presenter.getEditTextColor(state))
//            binding.loginPhonePinViewCodeFirstThreeNumbers.background.mutate()
//                .setTint(presenter.getEditTextColor(state))
//            binding.loginPhonePinViewCodeSecondThreeNumbers.background.mutate()
//                .setTint(presenter.getEditTextColor(state))
//            binding.loginPhonePinViewCodeFirstTwoNumbers.background.mutate()
//                .setTint(presenter.getEditTextColor(state))
//            binding.loginPhonePinViewCodeSecondTwoNumbers.background.mutate()
//                .setTint(presenter.getEditTextColor(state))
//
//            binding.loginPhonePinViewCode.background.mutate().setTintMode(PorterDuff.Mode.SRC_ATOP)
//            binding.loginPhonePinViewCodeFirstThreeNumbers.background.mutate()
//                .setTintMode(PorterDuff.Mode.SRC_ATOP)
//            binding.loginPhonePinViewCodeSecondThreeNumbers.background.mutate()
//                .setTintMode(PorterDuff.Mode.SRC_ATOP)
//            binding.loginPhonePinViewCodeFirstTwoNumbers.background.mutate()
//                .setTintMode(PorterDuff.Mode.SRC_ATOP)
//            binding.loginPhonePinViewCodeSecondTwoNumbers.background.mutate()
//                .setTintMode(PorterDuff.Mode.SRC_ATOP)
        }
    }

    override fun lightTheNameView(state: LoginInputPhoneState) {
        activity?.runOnUiThread {
            binding.loginNamePinInput.background.mutate().setTint(presenter.getEditTextColor(state))
            binding.loginNamePinInput.background.mutate().setTintMode(PorterDuff.Mode.SRC_ATOP)
        }
    }

    override fun changeButtonRequestCodeState(enabled: Boolean, textId: Int) {
        activity?.runOnUiThread {
//            if (enabled) {
//                binding.buttonLoginRequestCode.isEnabled = true
//                binding.buttonLoginRequestCode.background =
//                    getDrawable(context!!, R.drawable.ic_curved_gradient)
//            } else {
//                binding.buttonLoginRequestCode.isEnabled = false
//                binding.buttonLoginRequestCode.background =
//                    getDrawable(context!!, R.drawable.ic_gray_corner)
//            }
//
//            binding.buttonLoginRequestCode.setText(textId)
        }
    }

    override fun setTheUserAgreementSwitchState(state: Boolean) {
        activity?.runOnUiThread {
            binding.switchLoginAgreement.isChecked = state
        }
    }

    override fun startUserAgreementFragment() {
        activity?.runOnUiThread {
            loginActivityView.startUserAgreement()
        }
    }

    fun gotUpdateForUserAgreement(state: Boolean) {
        presenter.userAgreementNewState(state)
    }

    private fun getCurrentNumber(): String {
        return binding.mobilePhone.text.toString()
//        return (binding.loginPhonePinViewCode.text?.toString() ?: "") +
//                (binding.loginPhonePinViewCodeFirstThreeNumbers.text?.toString() ?: "") +
//                (binding.loginPhonePinViewCodeSecondThreeNumbers.text?.toString() ?: "") +
//                (binding.loginPhonePinViewCodeFirstTwoNumbers.text?.toString() ?: "") +
//                (binding.loginPhonePinViewCodeSecondTwoNumbers.text?.toString() ?: "")
    }
}

enum class LoginInputPhoneState {
    NONE, GREEN, RED
}