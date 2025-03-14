package com.example.flux.store.ui.purchasableworks

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.example.flux.common.util.ext.ViewExt.setSafeClickListener
import com.example.flux.store.databinding.ViewChipSearchConditionLabelBinding

class SearchConditionLabelChipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.webViewStyle,
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val binding: ViewChipSearchConditionLabelBinding by lazy {
        ViewChipSearchConditionLabelBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun initialize(title: String) {
        binding.chip.text = title
        binding.chip.setOnClickListener { }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        binding.chip.setSafeClickListener { listener?.onClick(it) }
    }
}
