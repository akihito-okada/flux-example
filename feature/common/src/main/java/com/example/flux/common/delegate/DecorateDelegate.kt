package com.example.flux.common.delegate

import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan

interface DecorateDelegate {

    fun decorateUnderLine(text: CharSequence): SpannableString {
        val spanStr = SpannableString(text)
        spanStr.setSpan(UnderlineSpan(), 0, spanStr.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spanStr
    }
}
