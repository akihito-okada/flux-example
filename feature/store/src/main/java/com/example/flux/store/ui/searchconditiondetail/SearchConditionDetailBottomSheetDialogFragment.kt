package com.example.flux.store.ui.searchconditiondetail

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.flux.common.R
import com.example.flux.common.compose.theme.AppTheme
import com.example.flux.common.flux.store.MainStore
import com.example.flux.common.util.ext.BottomSheetDialogExt.dismissOnCollapsed
import com.example.flux.common.util.navigator.Navigator
import com.example.flux.model.StoreSearchConditions
import com.example.flux.model.TagId
import com.example.flux.store.databinding.DialogFragmentSearchConditionDetailBinding
import com.example.flux.store.ui.searchconditiondetail.component.SearchConditionScreen
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class SearchConditionDetailBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogFragmentSearchConditionDetailBinding

    private val tagId by lazy { TagId.fromArguments(arguments = arguments) }

    @Inject
    lateinit var actionCreatorFactory: SearchConditionDetailBottomSheetDialogActionCreator.Factory
    private val actionCreator: SearchConditionDetailBottomSheetDialogActionCreator by lazy {
        actionCreatorFactory.create(
            lifecycle = lifecycle,
            tagId = tagId,
        )
    }

    @Inject
    lateinit var navigator: Navigator

    private val bottomSheetDialogBehavior: BottomSheetBehavior<FrameLayout>?
        get() = (dialog as? BottomSheetDialog)?.behavior
    private val store: SearchConditionDetailBottomSheetDialogStore by viewModels()
    private val mainStore by lazy { ViewModelProvider(requireActivity())[MainStore::class.java] }
    private var listener: SearchConditionDetailStateChangedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = when {
            context is SearchConditionDetailStateChangedListener -> context
            parentFragment is SearchConditionDetailStateChangedListener -> parentFragment as SearchConditionDetailStateChangedListener
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
                    SearchConditionScreen(
                        actionCreator = actionCreator,
                        store = store,
                        mainStore = mainStore,
                        onClickClose = remember {
                            {
                                dismiss()
                            }
                        },
                        onClickSearch = remember {
                            {
                                dismiss()
                                listener?.onSelectSearch(it)
                            }
                        },
                        onScrollState = remember {
                            { isTop ->
                                Timber.d("isTop $isTop")
                                bottomSheetDialogBehavior?.isDraggable = isTop
                            }
                        },
                    )
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFragmentSearchConditionDetailBinding.inflate(layoutInflater)
        return BottomSheetDialog(requireContext(), R.style.App_Widget_BottomSheetDialog_Rounded).also { dialog ->
            dialog.setContentView(binding.root)
            dialog.setOnShowListener {
                onDialogViewCreated()
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                dialog.dismissOnCollapsed()
            }
        }
    }

    companion object {
        private const val TAG_NAME = "SearchConditionDetailBottomSheetDialogFragment"

        fun show(fragmentManager: FragmentManager, tagId: TagId, searchConditions: StoreSearchConditions = StoreSearchConditions()) {
            if (fragmentManager.findFragmentByTag(TAG_NAME) != null) {
                return
            }
            instance(tagId, searchConditions).also {
                it.show(fragmentManager, TAG_NAME)
            }
        }

        private fun instance(tagId: TagId, searchConditions: StoreSearchConditions): SearchConditionDetailBottomSheetDialogFragment {
            return SearchConditionDetailBottomSheetDialogFragment().also {
                it.arguments = Bundle().also { bundle ->
                    searchConditions.toArguments(bundle)
                    tagId.toArguments(bundle)
                }
            }
        }
    }
}
