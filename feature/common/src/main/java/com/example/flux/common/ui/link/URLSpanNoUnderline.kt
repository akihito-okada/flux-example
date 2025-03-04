package com.example.flux.common.ui.link

import android.text.TextPaint
import android.text.style.URLSpan

class URLSpanNoUnderline(url: String?) : URLSpan(url) {
    override fun updateDrawState(paint: TextPaint) {
        super.updateDrawState(paint)
        paint.isUnderlineText = false
    }
}
