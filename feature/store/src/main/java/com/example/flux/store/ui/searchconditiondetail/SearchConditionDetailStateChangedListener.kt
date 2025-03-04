package com.example.flux.store.ui.searchconditiondetail

import com.example.flux.model.StoreSearchConditions

interface SearchConditionDetailStateChangedListener {
    fun onSelectSearch(storeSearchConditions: StoreSearchConditions)
}
