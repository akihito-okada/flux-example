package com.example.flux.store.ui.searchconditiondetail.component

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.flux.common.compose.ext.recomposeHighlighter
import com.example.flux.common.compose.theme.AppTheme
import com.example.flux.store.R

@Composable
fun SearchConditionHeaderCard(
    modifier: Modifier = Modifier,
    shouldShowRecomposeHighlighter: Boolean = false,
    count: Int,
    isExpanded: Boolean = false,
    @StringRes titleRes: Int = R.string.blank,
    onClick: (Boolean) -> Unit,
) {
    Card(
        modifier = modifier
            .recomposeHighlighter(shouldShowRecomposeHighlighter)
            .clickable { onClick.invoke(isExpanded.not()) },
        backgroundColor = AppTheme.colors.surfaceDialog,
        elevation = 0.dp,
    ) {
        ConstraintLayout(
            modifier = modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp,
                )
                .fillMaxWidth(),
        ) {
            val (titleLabel, countLabel, arrow) = createRefs()
            val title = stringResource(titleRes)
            Text(
                text = title,
                textAlign = TextAlign.Start,
                style = AppTheme.typography.SubtitleMedium16L24,
                color = AppTheme.colors.onSurfaceDark,
                modifier = Modifier
                    .constrainAs(titleLabel) {
                        top.linkTo(
                            anchor = parent.top,
                        )
                        bottom.linkTo(
                            anchor = parent.bottom,
                        )
                        start.linkTo(
                            anchor = parent.start,
                        )
                    },
            )
            if (count > 0) {
                Text(
                    text = stringResource(R.string.braces_format, count),
                    textAlign = TextAlign.Start,
                    style = AppTheme.typography.SubtitleMedium14L16,
                    color = AppTheme.colors.onAttentionInfo,
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                        )
                        .constrainAs(countLabel) {
                            start.linkTo(
                                anchor = titleLabel.end,
                            )
                            baseline.linkTo(
                                anchor = titleLabel.baseline,
                            )
                        },
                )
            }
            Image(
                modifier = Modifier
                    .constrainAs(arrow) {
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
                painter = painterResource(
                    id = when {
                        isExpanded -> R.drawable.ic_up_arrow_black_24dp
                        else -> R.drawable.ic_down_arrow_black_24dp
                    },
                ),
                contentDescription = title,
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun SearchConditionHeaderCardPreview() {
    AppTheme {
        SearchConditionHeaderCard(
            count = 3,
            titleRes = R.string.add_review_title,
        ) {}
    }
}
