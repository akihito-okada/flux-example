package com.example.flux.common.compose.component

import android.view.View
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.flux.common.compose.theme.AppTheme

@Composable
fun LoadingItem() {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally),
        factory = { context ->
            View(context)
        },
    )
}

@Preview
@Composable
fun LoadingItemPreview() {
    AppTheme {
        LoadingItem()
    }
}
