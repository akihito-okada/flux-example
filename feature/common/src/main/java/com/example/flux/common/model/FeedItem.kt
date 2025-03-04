package com.example.flux.common.model

import com.example.flux.model.BaseItem

sealed class FeedItem : BaseItem {
    data class EmptyItem(val emptyViewType: EmptyViewType = EmptyViewType.Unknown) : BaseItem
}
