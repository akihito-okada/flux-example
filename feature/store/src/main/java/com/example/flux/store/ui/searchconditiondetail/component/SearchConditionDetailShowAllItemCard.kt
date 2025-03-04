package com.example.flux.store.ui.searchconditiondetail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flux.common.R
import com.example.flux.common.compose.ext.recomposeHighlighter
import com.example.flux.common.compose.theme.AppTheme

@Composable
fun SearchConditionDetailShowAll(
    modifier: Modifier = Modifier,
    shouldShowRecomposeHighlighter: Boolean = false,
    isExpanded: Boolean = false,
    onClick: (Boolean) -> Unit = {},
) {
    Box(
        modifier = modifier
            .recomposeHighlighter(shouldShowRecomposeHighlighter)
            .clickable {
                onClick.invoke(
                    isExpanded.not(),
                )
            }
            .height(48.dp)
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            text = stringResource(if (isExpanded) R.string.show_less else R.string.show_more),
            color = AppTheme.colors.onSurfaceDark,
            style = AppTheme.typography.CaptionMedium12L16,
            textDecoration = TextDecoration.Underline,
        )
    }
}

@Preview
@Composable
fun SearchConditionDetailShowAllPreview() {
    AppTheme {
        SearchConditionDetailShowAll()
    }
}
