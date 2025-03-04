package com.example.flux.common.ui.item

import android.view.View
import com.example.flux.common.R
import com.example.flux.common.databinding.ItemUnknownBinding
import com.xwray.groupie.viewbinding.BindableItem

// 基本使わないはず
class UnknownItem constructor(val id: Int = R.layout.item_unknown) : BindableItem<ItemUnknownBinding>(
    id.toLong(),
) {

    override fun bind(binding: ItemUnknownBinding, position: Int) {
        // nothing to do
    }

    override fun getLayout(): Int = R.layout.item_unknown

    override fun initializeViewBinding(view: View): ItemUnknownBinding {
        return ItemUnknownBinding.bind(view)
    }
}
