package com.example.flux.store.ui.searchconditiondetail.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flux.common.compose.ext.recomposeHighlighter
import com.example.flux.common.compose.theme.AppTheme

@Composable
fun SearchConditionDividerCard(
    modifier: Modifier = Modifier,
    shouldShowRecomposeHighlighter: Boolean = false,
) {
    Divider(
        color = AppTheme.colors.borderSoft,
        modifier = modifier
            .recomposeHighlighter(shouldShowRecomposeHighlighter)
            .padding(
                top = 16.dp,
                bottom = 8.dp,
                start = 16.dp,
                end = 16.dp,
            ),
    )
}

@ExperimentalMaterialApi
@Preview
@Composable
fun SearchConditionDividerCardPreview() {
    AppTheme {
        SearchConditionDividerCard(modifier = Modifier)
    }
}
