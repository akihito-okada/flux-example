package com.example.flux.works.ui.toydetail

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.flux.common.compose.theme.AppTheme
import com.example.flux.common.databinding.FragmentComposeViewBinding
import com.example.flux.common.delegate.viewBinding
import com.example.flux.common.util.navigator.Navigator
import com.example.flux.model.util.EnvironmentConfig
import com.example.flux.common.model.FragmentOptions
import com.example.flux.model.TagId
import com.example.flux.model.ToyId
import com.example.flux.works.R
import com.example.flux.works.ui.toydetail.compose.ToyDetailScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ToyDetailFragment :
    Fragment(R.layout.fragment_compose_view) {

    private val binding: FragmentComposeViewBinding by viewBinding { FragmentComposeViewBinding.bind(it) }

    @Inject
    lateinit var environmentConfig: EnvironmentConfig

    @Inject
    lateinit var navigator: Navigator

    private val tagId by lazy { TagId.fromArguments(arguments = arguments) }
    private val toyId by lazy { ToyId.fromArguments(arguments = arguments) }

    @Inject
    lateinit var actionCreatorFactory: ToyDetailActionCreator.Factory
    private val actionCreator: ToyDetailActionCreator by lazy {
        actionCreatorFactory.create(
            lifecycle = lifecycle,
            tagId = tagId,
            toyId = toyId,
        )
    }

    private val toyDetailStore: ToyDetailStore by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner),
            )
            setContent {
                AppTheme {
                    ToyDetailScreen(
                        actionCreator = actionCreator,
                        store = toyDetailStore,
                        navigator = navigator,
                    )
                }
            }
        }
    }

    companion object {
        fun instance(fragmentOptions: FragmentOptions, toyId: ToyId): ToyDetailFragment {
            return ToyDetailFragment().also { fragment ->
                fragment.arguments = Bundle().also {
                    toyId.toArguments(it)
                    TagId(fragment.hashCode()).toArguments(it)
                    getFragmentOptions(fragmentOptions.referrerRes).toArguments(it)
                }
            }
        }

        private fun getFragmentOptions(referrerRes: Int): FragmentOptions {
            return FragmentOptions(
                nameRes = R.string.title_screen_toy_detail,
                referrerRes = referrerRes,
            )
        }
    }
}
