package com.example.flux.store.ui.searchconditiondetail.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.flux.common.R
import com.example.flux.common.compose.ext.recomposeHighlighter
import com.example.flux.common.compose.theme.AppTheme

@Composable
fun SearchConditionDetailHeader(
    modifier: Modifier = Modifier,
    shouldShowRecomposeHighlighter: Boolean = false,
    onClickClose: () -> Unit = {},
) {
    ConstraintLayout(
        modifier = modifier
            .recomposeHighlighter(shouldShowRecomposeHighlighter)
            .height(46.dp)
            .padding(horizontal = 2.dp)
            .fillMaxWidth(),
    ) {
        val (helpName, closeButton) = createRefs()
        Text(
            text = stringResource(R.string.store_refine_search_title),
            color = AppTheme.colors.onSurfaceDark,
            style = AppTheme.typography.SubtitleMedium16L24,
            modifier = Modifier
                .constrainAs(helpName) {
                    top.linkTo(
                        anchor = parent.top,
                    )
                    bottom.linkTo(
                        anchor = parent.bottom,
                    )
                    start.linkTo(
                        anchor = parent.start,
                        margin = 12.dp,
                    )
                    end.linkTo(
                        anchor = parent.end,
                        margin = 12.dp,
                    )
                },
        )
        TextButton(
            modifier = Modifier
                .constrainAs(closeButton) {
                    top.linkTo(
                        anchor = parent.top,
                    )
                    bottom.linkTo(
                        anchor = parent.bottom,
                    )
                    end.linkTo(
                        anchor = parent.end,
                    )
                },
            onClick = onClickClose,
        ) {
            Text(
                text = stringResource(R.string.close),
                color = AppTheme.colors.onSurfaceDeep,
                style = AppTheme.typography.BodyRegular14L22,
            )
        }
    }
}

@Preview
@Composable
fun HelpHeaderPreview() {
    AppTheme {
        SearchConditionDetailHeader()
    }
}
