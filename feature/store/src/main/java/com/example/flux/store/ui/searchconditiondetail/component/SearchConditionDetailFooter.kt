package com.example.flux.store.ui.searchconditiondetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.flux.common.R
import com.example.flux.common.compose.ext.recomposeHighlighter
import com.example.flux.common.compose.theme.AppTheme

@Composable
fun SearchConditionDetailFooter(
    modifier: Modifier = Modifier,
    shouldShowRecomposeHighlighter: Boolean = false,
    itemCount: Int = 0,
    onClickClear: () -> Unit = {},
    onClickSearch: () -> Unit = {},
) {
    ConstraintLayout(
        modifier = modifier
            .recomposeHighlighter(shouldShowRecomposeHighlighter)
            .height(90.dp)
            .padding(
                vertical = 16.dp,
            )
            .fillMaxWidth(),
    ) {
        val (clearButton, searchButton) = createRefs()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(120.dp)
                .height(48.dp)
                .constrainAs(clearButton) {
                    top.linkTo(
                        anchor = parent.top,
                    )
                    start.linkTo(
                        anchor = parent.start,
                    )
                }
                .clip(RoundedCornerShape(50))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClickClear,
                )
                .background(AppTheme.colors.surfaceBright)
                .border(width = 1.dp, shape = RoundedCornerShape(50), color = AppTheme.colors.borderMiddle),
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                text = stringResource(R.string.filter_clear_all),
                color = AppTheme.colors.onSurfaceDark,
                style = AppTheme.typography.SubtitleMedium16L24,
                textAlign = TextAlign.Center,
            )
        }
        Box(
            modifier = Modifier
                .height(48.dp)
                .constrainAs(searchButton) {
                    top.linkTo(
                        anchor = parent.top,
                    )
                    start.linkTo(
                        anchor = clearButton.end,
                        margin = 16.dp,
                    )
                    end.linkTo(
                        anchor = parent.end,
                    )
                    width = Dimension.fillToConstraints
                }
                .clip(RoundedCornerShape(50))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClickSearch,
                )
                .background(AppTheme.colors.surfaceDark),
        ) {
            Row(
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    text = stringResource(R.string.store_index_refine_search_btn),
                    color = AppTheme.colors.onBackgroundBright,
                    style = AppTheme.typography.SubtitleMedium16L24,
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 4.dp),
                    text = stringResource(R.string.braces_format_with_space, itemCount),
                    color = AppTheme.colors.onBackgroundBright,
                    style = AppTheme.typography.CaptionMedium12L16,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview
@Composable
fun SearchConditionDetailFooterPreview() {
    AppTheme {
        SearchConditionDetailFooter()
    }
}
