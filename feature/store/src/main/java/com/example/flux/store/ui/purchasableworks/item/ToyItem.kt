package com.example.flux.store.ui.purchasableworks.item

import android.view.View
import androidx.core.view.isVisible
import com.example.flux.common.delegate.DecorateDelegate
import com.example.flux.common.util.ext.ImageViewExt.setImageRectangle
import com.example.flux.common.util.ext.ImageViewExt.setImageRes
import com.example.flux.common.util.ext.ResourcesExt.screenWidth
import com.example.flux.common.util.ext.ViewExt.setSafeClickListener
import com.example.flux.common.util.recyclerview.EqualableContentsProvider
import com.example.flux.model.Toy
import com.example.flux.model.ToySaveRequest
import com.example.flux.store.R
import com.example.flux.store.databinding.ItemToyBinding
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem

class ToyItem(
    private val toy: Toy,
    private val spanSize: Int,
    private val screenType: ScreenType = ScreenType.Default,
    private val onClickKeep: (toySaveRequest: ToySaveRequest) -> Unit,
    private val onClickToyItem: (toy: Toy) -> Unit,
) : BindableItem<ItemToyBinding>(
    toy.id.value,
),
    EqualableContentsProvider,
    DecorateDelegate {

    override fun bind(binding: ItemToyBinding, position: Int) {
        with(binding) {
            val resources = binding.root.resources
            val basePadding = resources.getDimensionPixelSize(R.dimen.spacing_medium)
            val scaledWidth = (resources.screenWidth - basePadding * (spanSize + 1)) / spanSize
            toyImage.setImageRectangle(toy.image, scaledWidth)

            priceLabel.text = resources.getString(R.string.toy_price_format, toy.price)
            toyTitle.text = toy.name
            makerName.text = toy.makerName
            dimensions.isVisible = screenType.isPurchasableToys &&
                toy.dimensions.isNotEmpty()
            dimensions.text = toy.dimensions
            updateSave(toy.hasSaved)
            keepLayout.setSafeClickListener {
                val hasSaved = !keepLayout.isSelected
                onClickKeep.invoke(ToySaveRequest(toy, hasSaved))
            }
            card.setSafeClickListener {
                onClickToyItem.invoke(toy)
            }
        }
    }

    override fun getChangePayload(newItem: Item<*>): Any? {
        return when {
            newItem !is ToyItem -> null
            isChangeHasSaved(newItem) -> ItemPayload.SaveToy(newItem.toy.hasSaved)
            else -> null
        }
    }

    private fun isChangeHasSaved(newItem: ToyItem): Boolean {
        return toy.hasSaved != newItem.toy.hasSaved
    }

    override fun bind(binding: ItemToyBinding, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            bind(binding, position)
        } else {
            payloads.distinct().forEach { payload ->
                when (payload) {
                    is ItemPayload.SaveToy -> {
                        binding.updateSave(payload.hasSaved)
                    }
                }
            }
        }
    }

    private fun ItemToyBinding.updateSave(hasSaved: Boolean) {
        keepLayout.isSelected = hasSaved
        saveButton.setImageRes(
            when {
                hasSaved -> R.drawable.ic_keep_on_black_24dp
                else -> R.drawable.ic_keep_off_white_24dp
            },
        )
    }


    private sealed class ItemPayload {
        data class SaveToy(val hasSaved: Boolean) : ItemPayload()
    }

    override fun providerEqualableContents(): Array<*> =
        arrayOf(toy.id.value, toy.hasSaved)

    override fun equals(other: Any?): Boolean = isSameContents(other)

    override fun hashCode(): Int = contentsHash()

    override fun getLayout(): Int = R.layout.item_toy

    override fun getSpanSize(spanCount: Int, position: Int): Int = spanSize

    override fun initializeViewBinding(view: View): ItemToyBinding {
        return ItemToyBinding.bind(view)
    }

    enum class ScreenType {
        PurchasableToys, Default;

        val isPurchasableToys get() = this == PurchasableToys
    }
}
