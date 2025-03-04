package com.example.flux.common.model

import androidx.compose.runtime.Immutable
import com.example.flux.model.BaseItem

@Immutable
data class FeedItemsUpdateRequest(
    val feedItems: List<BaseItem> = listOf(),
    var isInitial: Boolean = false,
    var isFeedUpdated: Boolean = false, // MediatorLiveData用
    val isAllItemLoaded: Boolean = false,
) : BaseItem
