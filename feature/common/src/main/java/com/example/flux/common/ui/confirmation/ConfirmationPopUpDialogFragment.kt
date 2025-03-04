package com.example.flux.common.ui.confirmation

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.flux.common.R
import com.example.flux.common.databinding.DialogFragmentConfirmationPopUpBinding
import com.example.flux.common.util.ext.ViewExt.setSafeClickListener
import com.example.flux.common.util.ext.ViewExt.toGone
import com.example.flux.common.util.ext.ViewExt.toVisible
import com.example.flux.common.model.ConfirmationDialogMessage
import com.example.flux.common.model.ConfirmationDialogType

class ConfirmationPopUpDialogFragment : DialogFragment() {

    private lateinit var binding: DialogFragmentConfirmationPopUpBinding

    private var listener: ConfirmationDialogListener? = null
    private val dialogMessage: ConfirmationDialogMessage
        get() = ConfirmationDialogMessage.fromArguments(arguments)
    private val dialogType: ConfirmationDialogType
        get() = ConfirmationDialogType.fromOrdinal(dialogMessage.confirmationDialogTypeOrdinal)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ConfirmationDialogListener) {
            listener = context
            return
        }
        if (parentFragment is ConfirmationDialogListener) {
            listener = parentFragment as ConfirmationDialogListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFragmentConfirmationPopUpBinding.inflate(layoutInflater)

        binding.also {
            it.title.also { view ->
                when {
                    dialogMessage.shouldShowTitle -> {
                        view.toVisible()
                        view.text = dialogMessage.getTitle(resources)
                    }

                    else -> view.toGone()
                }
            }
            it.message.also { view ->
                when {
                    dialogMessage.shouldShowMessage -> {
                        view.toVisible()
                        it.message.text = dialogMessage.getMessage(resources)
                    }

                    else -> view.toGone()
                }
            }
            it.positiveButton.also { button ->
                button.text = dialogMessage.getPositiveButtonText(resources)
                button.setSafeClickListener {
                    listener?.proceedForConfirmationDialog(dialogType)
                    dismiss()
                }
            }
            it.negativeButton.also { button ->
                if (dialogMessage.shouldShowNegativeButton) {
                    button.toVisible()
                    button.text = dialogMessage.getNegativeButtonText(resources)
                    button.setSafeClickListener {
                        listener?.cancelForConfirmationDialog()
                        dismiss()
                    }
                } else {
                    button.toGone()
                    button.setSafeClickListener(null)
                }
            }
        }

        return Dialog(requireContext(), R.style.Theme_MaterialComponents_Dialog).also {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setContentView(binding.root)
        }
    }

    companion object {

        private const val TAG_NAME = "ConfirmationPopUpDialogFragment"

        fun show(fragmentManager: FragmentManager, dialogMessage: ConfirmationDialogMessage) {
            if (fragmentManager.findFragmentByTag(TAG_NAME) != null) {
                return
            }
            instance(dialogMessage).also {
                it.show(fragmentManager, TAG_NAME)
            }
        }

        private fun instance(confirmationDialogMessage: ConfirmationDialogMessage): ConfirmationPopUpDialogFragment {
            return ConfirmationPopUpDialogFragment().also { fragment ->
                fragment.arguments = Bundle().also { bundle ->
                    confirmationDialogMessage.toArguments(bundle)
                }
            }
        }
    }
}
