package com.example.flux.store.ui.purchasableworks

import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.flux.common.delegate.groupAdapter
import com.example.flux.common.delegate.viewBinding
import com.example.flux.common.ui.item.EmptyItem
import com.example.flux.common.ui.item.UnknownItem
import com.example.flux.common.flux.store.MainStore
import com.example.flux.common.util.ext.RecyclerViewExt.scrollToTop
import com.example.flux.common.util.ext.ResourcesExt.screenWidth
import com.example.flux.common.util.ext.StaggeredGridLayoutManagerExt.findFirstVisiblePosition
import com.example.flux.common.util.ext.SwipeRefreshLayoutExt.hide
import com.example.flux.common.util.ext.ViewExt.setSafeClickListener
import com.example.flux.common.util.ext.ViewExt.toGone
import com.example.flux.common.util.lifecycle.EventObserver
import com.example.flux.common.util.navigator.Navigator
import com.example.flux.common.util.recyclerview.InfiniteScrollListener
import com.example.flux.model.BaseItem
import com.example.flux.common.model.EmptyViewType
import com.example.flux.common.model.FabState
import com.example.flux.common.model.FeedItem
import com.example.flux.common.model.FragmentOptions
import com.example.flux.model.PriceRange
import com.example.flux.model.PurchasableWorksChipsItem
import com.example.flux.model.PurchasableWorksSortType
import com.example.flux.model.StoreSearchConditions
import com.example.flux.model.TagId
import com.example.flux.model.Toy
import com.example.flux.model.analytics.ScrollPosition
import com.example.flux.store.R
import com.example.flux.store.databinding.FragmentPurchasableWorksBinding
import com.example.flux.store.ui.purchasableworks.item.ToyItem
import com.example.flux.store.ui.searchconditiondetail.SearchConditionDetailStateChangedListener
import com.example.flux.store.util.showSearchConditionBottomSheetDialogFragment
import com.xwray.groupie.Item
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class PurchasableWorksFragment :
    Fragment(R.layout.fragment_purchasable_works),
    SearchConditionDetailStateChangedListener {

    private val binding: FragmentPurchasableWorksBinding by viewBinding { FragmentPurchasableWorksBinding.bind(it) }

    @Inject
    lateinit var navigator: Navigator

    private val tagId by lazy { TagId.fromArguments(arguments = arguments) }

    @Inject
    lateinit var actionCreatorFactory: PurchasableWorksActionCreator.Factory
    private val actionCreator: PurchasableWorksActionCreator by lazy {
        actionCreatorFactory.create(
            lifecycle = lifecycle,
            tagId = tagId,
        )
    }

    private val purchasableWorksStore: PurchasableWorksStore by viewModels()

    private val mainStore by lazy { ViewModelProvider(requireActivity())[MainStore::class.java] }

    private val groupAdapter by groupAdapter()
    private var layoutManager: StaggeredGridLayoutManager? = null
    private var maxScrollPosition = 0

    private val initialConditions get() = StoreSearchConditions.fromArguments(arguments)
    private val fragmentOptions get() = FragmentOptions.fromArguments(arguments)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restore maxScrollPosition
        maxScrollPosition = purchasableWorksStore.scrollPosition.maxVisiblePosition
        val listener = object : InfiniteScrollListener(
            layoutManager = StaggeredGridLayoutManager(STAGGERED_SPAN_COUNT, RecyclerView.VERTICAL).also {
                layoutManager = it
            },
            direction = LoadOnScrollDirection.BOTTOM,
        ) {
            override fun onLoadMore(page: Int, totalItemCount: Int) {
                if (!mainStore.isNetworkAvailable) {
                    clear()
                    return
                }
                actionCreator.loadPurchasableWorks(
                    conditions = purchasableWorksStore.conditions,
                    offset = totalItemCount - 1,
                )
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val currentPosition = layoutManager.findFirstVisiblePosition()
                if (currentPosition > maxScrollPosition) {
                    maxScrollPosition = currentPosition
                }
            }

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)
                actionCreator.updateStoreFabStateIfNeeded(dy)
            }
        }
        binding.also {
            it.toolbar.setup()
            it.sortButton.setSafeClickListener { view ->
                showSortPopup(view)
            }
            it.recyclerView.also { view ->
                view.adapter = groupAdapter
                view.layoutManager = layoutManager
                view.addOnScrollListener(listener)
            }
            it.pullToRefresh.hide()
            it.pullToRefresh.setOnRefreshListener {
                binding.loadingView.toGone()
                actionCreator.loadPurchasableWorks(
                    conditions = purchasableWorksStore.conditions,
                )
            }
            it.fabSearch.setSafeClickListener {
                showSearchConditionBottomSheetDialogFragment(
                    tagId = tagId,
                    searchConditions = purchasableWorksStore.conditions,
                )
            }
        }
        purchasableWorksStore.also { store ->
            store.loadingState.observe(
                viewLifecycleOwner,
                EventObserver { loadingState ->
                    binding.loadingView.isVisible = loadingState.isLoading
                    binding.pullToRefresh.isRefreshing = false
                },
            )
            store.searchConditionsUpdated.observe(
                viewLifecycleOwner,
            ) { conditions ->
                updateChips(conditions = conditions)
                updateFilterNotificationDot(conditions = conditions)
                updateSortLabel(conditions = conditions)
            }
            store.sortTypeChanged.observe(
                viewLifecycleOwner,
            ) {
                binding.recyclerView.scrollToTop()
            }
            store.purchasableWorksUpdated.observe(
                viewLifecycleOwner,
            ) {
                updatePurchasableWorks(it.feedItems)
                binding.worksCount.text = getString(R.string.store_search_result_number, store.numTotalWorks)
            }
            store.isAllItemLoaded.observe(
                viewLifecycleOwner,
            ) { isAllItemLoaded ->
                listener.toggle(isAllItemLoaded)
            }
        }
        mainStore.also { store ->
            store.searchFabStateChanged.observe(
                viewLifecycleOwner,
            ) {
                toggleFabSearch(it)
            }
        }
        if (purchasableWorksStore.isNotInitialized) {
            actionCreator.loadPurchasableWorks(initialConditions)
        }
    }

    private fun toggleFabSearch(fabState: FabState) {
        when (fabState) {
            FabState.Expanded -> expandFabSearch()
            FabState.Collapsed -> collapseFabSearch()
        }
    }

    private fun expandFabSearch() {
        if (binding.fabSearch.text.isNotEmpty()) {
            return
        }
        TransitionManager.beginDelayedTransition(binding.bottomLayout)
        binding.fabSearch.text = resources.getString(R.string.store_index_fab_search)
        binding.fabSearch.iconPadding = resources.getDimensionPixelSize(R.dimen.extended_fab_expanded_padding)
    }

    private fun collapseFabSearch() {
        if (binding.fabSearch.text.isEmpty()) {
            return
        }
        TransitionManager.beginDelayedTransition(binding.bottomLayout)
        binding.fabSearch.text = ""
        binding.fabSearch.iconPadding = resources.getDimensionPixelSize(R.dimen.extended_fab_collapsed_padding)
    }

    private fun updateSortLabel(conditions: StoreSearchConditions) {
        binding.sortButton.text = resources.getString(conditions.selectedSortType.labelRes)
    }

    private fun updateFilterNotificationDot(conditions: StoreSearchConditions) {
        binding.notificationDot.isGone = conditions.isInitialCondition
    }

    private fun updateChips(conditions: StoreSearchConditions) {
        val items = conditions.getChipsItems()
        binding.chipLayout.isVisible = items.isNotEmpty()
        binding.chipGroup.also { group ->
            group.removeAllViews()
            val context = group.context
            items.forEach { item ->
                group.addView(
                    when {
                        item.isClearAllItem -> SearchConditionAllClearChipView(context).also { view ->
                            view.initialize(item.title(resources))
                            view.setOnClickListener {
                                updateConditions(item, conditions)
                            }
                        }

                        else -> SearchConditionLabelChipView(context).also { view ->
                            view.initialize(item.title(resources))
                            view.setOnClickListener {
                                updateConditions(item, conditions)
                            }
                        }
                    },
                )
            }
        }
    }

    private fun updateConditions(item: PurchasableWorksChipsItem, conditions: StoreSearchConditions) {
        when (item) {
            is PurchasableWorksChipsItem.ClearAllItem -> {
                loadPurchasableWorksWithConditionsOld(
                    StoreSearchConditions(
                        selectedSortType = conditions.selectedSortType,
                        selectedPriceRange = PriceRange(revision = Random.nextInt()),
                    ),
                )
            }

            is PurchasableWorksChipsItem.CategoryItem -> {
                val selectedCategories = conditions.selectedCategories.toMutableList()
                selectedCategories.remove(item.category)
                loadPurchasableWorksWithConditionsOld(conditions.copy(selectedCategories = selectedCategories))
            }

            is PurchasableWorksChipsItem.ColorCategoryItem -> {
                val selectedColorCategories = conditions.selectedColorCategories.toMutableList()
                selectedColorCategories.remove(item.colorCategory)
                loadPurchasableWorksWithConditionsOld(conditions.copy(selectedColorCategories = selectedColorCategories))
            }

            is PurchasableWorksChipsItem.MaterialsTechniqueItem -> {
                val selectedMaterialsTechniques = conditions.selectedMaterialsTechniques.toMutableList()
                selectedMaterialsTechniques.remove(item.materialsTechnique)
                loadPurchasableWorksWithConditionsOld(conditions.copy(selectedMaterialsTechniques = selectedMaterialsTechniques))
            }

            is PurchasableWorksChipsItem.PriceRangeItem -> {
                loadPurchasableWorksWithConditionsOld(conditions.copy(selectedPriceRange = PriceRange()))
            }

            is PurchasableWorksChipsItem.ProductionYearsTypeItem -> {
                val selectedProductionYearsTypes = conditions.selectedProductionYearsTypes.toMutableList()
                selectedProductionYearsTypes.remove(item.productionYearsType)
                loadPurchasableWorksWithConditionsOld(conditions.copy(selectedProductionYearsTypes = selectedProductionYearsTypes))
            }
        }
    }

    private fun loadPurchasableWorksWithConditionsOld(conditions: StoreSearchConditions) {
        val conditionsOld = purchasableWorksStore.conditions
        actionCreator.loadPurchasableWorks(conditions, conditionsOld)
        binding.recyclerView.scrollToTop()
    }

    override fun onPause() {
        super.onPause()
        val scrollPosition = ScrollPosition(
            lastVisiblePosition = layoutManager.findFirstVisiblePosition(),
            maxVisiblePosition = maxScrollPosition,
        )
        actionCreator.loadCurrentScrollPosition(scrollPosition)
    }

    private fun updatePurchasableWorks(
        feedItems: List<BaseItem>,
    ) {
        val items = feedItems.mapIndexed { index, feedItem ->
            when (feedItem) {
                is Toy -> feedItem.toItem()
                is FeedItem.EmptyItem -> EmptyItem(index.toLong(), EmptyViewType.PurchasableWorks)
                else -> UnknownItem(index)
            }
        }
        groupAdapter.update(items)
    }

    private fun Toolbar.setup() {
        title = getString(fragmentOptions.titleRes)

        val screenWidth = resources.screenWidth
        val thresholdScreenWidth = resources.getDimensionPixelSize(R.dimen.standard_threshold_screen_width)
        if (screenWidth < thresholdScreenWidth) {
            binding.sortButton.maxWidth = resources.getDimensionPixelSize(R.dimen.purchasable_work_sort_menu_max_width)
        }
    }

    private fun Toy.toItem(): Item<*> {
        return ToyItem(
            toy = this,
            spanSize = STAGGERED_SPAN_COUNT,
            screenType = ToyItem.ScreenType.PurchasableToys,
            onClickKeep = {
                actionCreator.keepWork(it)
            },
        ) {
            navigator.navigateToToyDetail(fragmentOptions, it)
        }
    }

    // Display anchored popup menu based on view selected
    private fun showSortPopup(view: View) {
        val wrapper = ContextThemeWrapper(context, R.style.Widget_AppCompat_PopupMenu)
        val popup = PopupMenu(wrapper, view)
        popup.menuInflater.inflate(R.menu.menu_purchasable_works, popup.menu)

        // Setup menu item selection
        popup.setOnMenuItemClickListener { item ->
            val sortType = getSortTypeFromItemId(item.itemId)
            if (sortType == purchasableWorksStore.sortType) {
                // 同じsortTypeだったら何もしない
                return@setOnMenuItemClickListener false
            }
            val conditions = purchasableWorksStore.conditions.copy(selectedSortType = sortType)
            actionCreator.loadSortType(sortType = sortType)
            loadPurchasableWorksWithConditionsOld(conditions = conditions)
            return@setOnMenuItemClickListener true
        }
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show()
    }

    private fun getSortTypeFromItemId(itemId: Int): PurchasableWorksSortType {
        return PurchasableWorksSortType.fromOrdinal(
            when (itemId) {
                R.id.date -> 0
                R.id.price_desc -> 1
                R.id.price_asc -> 2
                else -> 0
            },
        )
    }

    override fun onSelectSearch(storeSearchConditions: StoreSearchConditions) {
        actionCreator.loadPurchasableWorks(
            conditions = storeSearchConditions,
        )
        binding.recyclerView.scrollToTop()
    }

    companion object {

        private const val STAGGERED_SPAN_COUNT = 2

        fun instance(fragmentOptions: FragmentOptions, conditions: StoreSearchConditions): PurchasableWorksFragment {
            return PurchasableWorksFragment().also { fragment ->
                fragment.arguments = Bundle().also {
                    TagId(fragment.hashCode()).toArguments(it)
                    getFragmentOptions(fragmentOptions.nameRes).toArguments(it)
                    conditions.toArguments(it)
                }
            }
        }

        private fun getFragmentOptions(referrerRes: Int): FragmentOptions {
            return FragmentOptions(
                nameRes = R.string.title_screen_toy_store,
                titleRes = R.string.store_search_result_title,
                referrerRes = referrerRes,
                isNowPlayingVisible = true,
            )
        }
    }
}
