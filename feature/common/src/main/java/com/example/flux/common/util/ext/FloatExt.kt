package com.example.flux.common.util.ext

import android.content.res.Resources
import android.util.TypedValue
import android.util.TypedValue.applyDimension

object FloatExt {
    fun Float.toDp(): Float {
        return applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)
    }
}
