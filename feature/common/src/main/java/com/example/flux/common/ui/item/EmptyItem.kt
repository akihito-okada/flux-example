package com.example.flux.common.ui.item

import android.view.View
import androidx.core.view.isVisible
import com.example.flux.common.R
import com.example.flux.common.databinding.ItemEmptyBinding
import com.example.flux.common.util.ext.ViewExt.setSafeClickListener
import com.example.flux.common.util.ext.ViewExt.setStaggeredGridLayoutFullSpan
import com.example.flux.common.util.ext.ViewExt.toGone
import com.example.flux.common.util.ext.ViewExt.toVisible
import com.example.flux.common.util.recyclerview.EqualableContentsProvider
import com.example.flux.common.model.EmptyViewType
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

data class EmptyItem(
    var index: Long = Long.MAX_VALUE,
    val emptyViewType: EmptyViewType,
    private val onClick: (() -> Unit)? = null,
) : BindableItem<ItemEmptyBinding>(
    index + emptyViewType.hashCode().toLong(),
),
    EqualableContentsProvider {

    override fun bind(binding: ItemEmptyBinding, position: Int) {
        // StaggeredGridLayoutManagerのときはFullSpanにする
        binding.root.setStaggeredGridLayoutFullSpan()

        val resources = binding.root.resources
        val title = resources.getString(emptyViewType.titleRes)

        binding.paddingLayout.isVisible = emptyViewType.hasHeaderPadding

        binding.title.also {
            when {
                title.isEmpty() -> it.toGone()
                else -> {
                    it.text = title
                    it.toVisible()
                }
            }
        }

        val message = resources.getString(emptyViewType.messageRes)
        binding.message.text = message
        binding.message.isVisible = message.isNotEmpty()

        val navigateAction = resources.getString(emptyViewType.navigateActionRes)
        binding.button.also {
            when {
                navigateAction.isEmpty() -> it.toGone()
                else -> {
                    it.toVisible()
                    it.text = navigateAction
                }
            }
        }
        binding.button.setSafeClickListener {
            onClick?.invoke()
        }
    }

    override fun unbind(holder: GroupieViewHolder<ItemEmptyBinding>) {
        super.unbind(holder)
        holder.binding.also {
            it.button.setSafeClickListener(null)
        }
    }

    override fun providerEqualableContents(): Array<*> =
        arrayOf(index)

    override fun equals(other: Any?): Boolean = isSameContents(other)

    override fun hashCode(): Int = contentsHash()

    override fun getLayout(): Int = R.layout.item_empty

    override fun initializeViewBinding(view: View): ItemEmptyBinding {
        return ItemEmptyBinding.bind(view)
    }
}
