package com.example.flux.store.util

import androidx.fragment.app.Fragment
import com.example.flux.model.StoreSearchConditions
import com.example.flux.model.TagId
import com.example.flux.store.ui.searchconditiondetail.SearchConditionDetailBottomSheetDialogFragment

fun Fragment.showSearchConditionBottomSheetDialogFragment(
    tagId: TagId,
    searchConditions: StoreSearchConditions = StoreSearchConditions(),
) {
    SearchConditionDetailBottomSheetDialogFragment.show(childFragmentManager, tagId, searchConditions)
}
