package com.example.flux.common.ui.messagepopup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.flux.common.R
import com.example.flux.common.compose.theme.AppTheme
import com.example.flux.common.databinding.DialogFragmentComposeViewBinding
import com.example.flux.common.ui.messagepopup.item.MessagePopUpScreen
import com.example.flux.common.model.DialogMessage
import com.example.flux.model.TagId
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagePopUpDialogFragment : DialogFragment() {

    private lateinit var binding: DialogFragmentComposeViewBinding

    private val dialogMessage: DialogMessage by lazy {
        DialogMessage.fromArguments(arguments)
    }

    private var listener: MessagePopUpListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = when {
            parentFragment is MessagePopUpListener -> parentFragment as MessagePopUpListener
            context is MessagePopUpListener -> context
            else -> null
        }
    }

    private fun onDialogViewCreated() {
        binding.composeView.also {
            it.setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(lifecycle),
            )
            it.setContent {
                AppTheme {
                    MessagePopUpScreen(
                        dialogMessage = dialogMessage,
                        listener,
                    ) {
                        dismiss()
                    }
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFragmentComposeViewBinding.inflate(layoutInflater)
        return Dialog(requireContext(), R.style.Theme_MaterialComponents_Dialog).also {
            it.setContentView(binding.root)
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            onDialogViewCreated()
        }
    }

    companion object {

        private const val TAG_NAME = "MessagePopUpDialogFragment"

        fun show(fragmentManager: FragmentManager, dialogMessage: DialogMessage, isCancelable: Boolean) {
            if (fragmentManager.findFragmentByTag(TAG_NAME) != null) {
                return
            }
            instance(dialogMessage).also {
                it.isCancelable = isCancelable
                it.show(fragmentManager, TAG_NAME)
            }
        }

        fun instance(dialogMessage: DialogMessage): MessagePopUpDialogFragment {
            return MessagePopUpDialogFragment().also { fragment ->
                fragment.arguments = Bundle().also {
                    TagId(fragment.hashCode()).toArguments(it)
                    dialogMessage.toArguments(it)
                }
            }
        }
    }
}
