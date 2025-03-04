package com.example.flux.common.ui

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.flux.common.R
import com.example.flux.common.model.DialogMessage
import com.example.flux.model.TagId
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MessageDialogFragment : DialogFragment() {

    private val dialogMessage: DialogMessage by lazy {
        DialogMessage.fromArguments(arguments)
    }

    private var listener: MessageDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = when {
            parentFragment is MessageDialogListener -> parentFragment as MessageDialogListener
            context is MessageDialogListener -> context
            else -> null
        }
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.App_Widget_MaterialAlertDialog_Message)
        val message = dialogMessage.getMessage(resources)
        val title = dialogMessage.getTitle(resources)
        when {
            title.isEmpty().not() -> builder.setTitle(title)
            else -> {
                // no-op
            }
        }
        when {
            message.isEmpty().not() -> builder.setMessage(message)
            else -> {
                // no-op
            }
        }
        builder.setPositiveButton(getString(dialogMessage.positiveTextRestId)) { _, _ -> listener?.onResultMessageDialog(dialogMessage, MessageDialogResult.OnClickPositive) }
        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.onResultMessageDialog(dialogMessage, MessageDialogResult.OnDismiss)
    }

    interface MessageDialogListener {
        fun onResultMessageDialog(
            dialogMessage: DialogMessage,
            result: MessageDialogResult,
        )
    }

    sealed class MessageDialogResult {
        object OnClickPositive : MessageDialogResult()
        object OnDismiss : MessageDialogResult()

        val isPositive: Boolean get() = this == OnClickPositive
    }

    companion object {

        private const val TAG_NAME = "MessageDialogFragment"

        fun show(fragmentManager: FragmentManager, dialogMessage: DialogMessage) {
            if (fragmentManager.findFragmentByTag(TAG_NAME) != null) {
                return
            }
            instance(dialogMessage).also {
                it.show(fragmentManager, TAG_NAME)
            }
        }

        private fun instance(dialogMessage: DialogMessage): MessageDialogFragment {
            return MessageDialogFragment().also { fragment ->
                fragment.arguments = Bundle().also {
                    TagId(fragment.hashCode()).toArguments(it)
                    dialogMessage.toArguments(it)
                }
            }
        }
    }
}
