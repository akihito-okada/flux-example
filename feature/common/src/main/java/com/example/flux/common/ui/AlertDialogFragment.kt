package com.example.flux.common.ui

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.flux.common.R
import com.example.flux.common.model.AlertDialogMessage
import com.example.flux.model.TagId
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AlertDialogFragment : DialogFragment() {

    private val dialogMessage: AlertDialogMessage by lazy {
        AlertDialogMessage.fromArguments(arguments)
    }

    private var listener: AlertDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = when {
            parentFragment is AlertDialogListener -> parentFragment as AlertDialogListener
            context is AlertDialogListener -> context
            else -> null
        }
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.App_Widget_MaterialAlertDialog_Message,
        )
        when {
            dialogMessage.isTitleEmpty().not() -> builder.setTitle(getString(dialogMessage.titleResId))
            else -> {
                // no-op
            }
        }
        when {
            dialogMessage.isMessageEmpty().not() -> builder.setMessage(getString(dialogMessage.messageResId))
            else -> {
                // no-op
            }
        }
        builder.setPositiveButton(getString(dialogMessage.positiveTextRestId)) { _, _ -> listener?.onResultAlertDialog(dialogMessage, AlertDialogResult.OnClickPositive) }
        builder.setNegativeButton(getString(dialogMessage.negativeTextResId)) { _, _ -> listener?.onResultAlertDialog(dialogMessage, AlertDialogResult.OnClickNegative) }
        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onResultAlertDialog(dialogMessage, AlertDialogResult.OnDismiss)
    }

    interface AlertDialogListener {
        fun onResultAlertDialog(
            alertDialogMessage: AlertDialogMessage,
            result: AlertDialogResult,
        )
    }

    sealed class AlertDialogResult {
        object OnClickPositive : AlertDialogResult()
        object OnClickNegative : AlertDialogResult()
        object OnDismiss : AlertDialogResult()

        val isPositive: Boolean get() = this == OnClickPositive
    }

    companion object {

        private const val TAG_NAME = "AlertDialogFragment"

        fun show(fragmentManager: FragmentManager, alertDialogMessage: AlertDialogMessage) {
            if (fragmentManager.findFragmentByTag(TAG_NAME) != null) {
                return
            }
            instance(alertDialogMessage).also {
                it.show(fragmentManager, TAG_NAME)
            }
        }

        private fun instance(alertDialogMessage: AlertDialogMessage): AlertDialogFragment {
            return AlertDialogFragment().also { fragment ->
                fragment.arguments = Bundle().also {
                    TagId(fragment.hashCode()).toArguments(it)
                    alertDialogMessage.toArguments(it)
                }
            }
        }
    }
}
