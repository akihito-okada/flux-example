package com.example.flux.common.delegate

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.example.flux.common.R

interface ClipboardDelegate {

    fun copyToClipboard(context: Context, text: String) {
        val label = context.getString(R.string.lbl_copy)
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
    }
}
