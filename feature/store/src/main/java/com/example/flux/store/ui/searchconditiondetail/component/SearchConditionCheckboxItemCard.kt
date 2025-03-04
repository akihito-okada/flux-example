package com.example.flux.store.ui.searchconditiondetail.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Checkbox
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.flux.common.compose.ext.recomposeHighlighter
import com.example.flux.common.compose.theme.AppTheme

@Composable
fun SearchConditionCheckboxItemCard(
    modifier: Modifier = Modifier,
    shouldShowRecomposeHighlighter: Boolean = false,
    title: String,
    description: String = "",
    @DrawableRes iconRes: Int = 0,
    isSelected: Boolean,
    onChecked: (Boolean) -> Unit,
    onClickLink: (() -> Unit)? = null,
) {
    var checked by remember(isSelected) { mutableStateOf(isSelected) }
    ConstraintLayout(
        modifier
            .recomposeHighlighter(shouldShowRecomposeHighlighter)
            .toggleable(
                value = checked,
                role = Checkbox,
                onValueChange = {
                    checked = it
                    onChecked.invoke(checked)
                },
            )
            .fillMaxWidth(),
    ) {
        val (checkBox, titleLabel, icon, descriptionLabel) = createRefs()
        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = it
                onChecked.invoke(checked)
            },
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    end = 8.dp,
                    top = 8.dp,
                    bottom = 8.dp,
                )
                .constrainAs(checkBox) {
                    top.linkTo(
                        anchor = parent.top,
                    )
                    start.linkTo(anchor = parent.start)
                },
            colors = CheckboxDefaults.colors(
                checkedColor = AppTheme.colors.attention,
                checkmarkColor = Color.White,
                uncheckedColor = AppTheme.colors.onSurfaceDeep,
            ),
        )
        val shouldShowIcon = iconRes != 0
        Text(
            text = title,
            textAlign = TextAlign.Start,
            style = AppTheme.typography.BodyRegular14L22,
            color = AppTheme.colors.onSurfaceDark,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(
                    start = 8.dp,
                    end = 12.dp,
                )
                .constrainAs(titleLabel) {
                    top.linkTo(
                        anchor = checkBox.top,
                    )
                    bottom.linkTo(checkBox.bottom)
                    start.linkTo(anchor = checkBox.end)
                    end.linkTo(anchor = if (shouldShowIcon) icon.start else parent.end)
                    width = Dimension.fillToConstraints
                },
        )
        if (shouldShowIcon) {
            Image(
                modifier = Modifier
                    .padding(
                        end = 55.dp,
                    )
                    .constrainAs(icon) {
                        top.linkTo(
                            anchor = titleLabel.top,
                        )
                        bottom.linkTo(
                            anchor = titleLabel.bottom,
                        )
                        end.linkTo(
                            anchor = parent.end,
                        )
                    },
                painter = painterResource(id = iconRes),
                contentDescription = title,
            )
        }
        val shouldShowDescription = description != ""
        val hasLink = onClickLink != null
        if (shouldShowDescription) {
            val textModifier = if (hasLink) {
                Modifier.clickable {
                    onClickLink?.invoke()
                }
            } else {
                Modifier
            }
            val textDecoration = if (hasLink) TextDecoration.Underline else null
            Text(
                text = description,
                textAlign = TextAlign.Start,
                style = AppTheme.typography.CaptionRegular12L16,
                color = AppTheme.colors.onSurfaceDeep,
                overflow = TextOverflow.Ellipsis,
                textDecoration = textDecoration,
                modifier = textModifier
                    .padding(
                        start = 8.dp,
                        end = 12.dp,
                        top = 8.dp,
                        bottom = 8.dp,
                    )
                    .constrainAs(descriptionLabel) {
                        top.linkTo(
                            anchor = titleLabel.bottom,
                        )
                        bottom.linkTo(
                            anchor = parent.bottom,
                        )
                        start.linkTo(anchor = checkBox.end)
                        end.linkTo(anchor = if (shouldShowIcon) icon.start else parent.end)
                        width = Dimension.fillToConstraints
                    },
            )
        }
    }
}

@Preview
@Composable
fun SearchConditionCheckboxItemCardColorPreview() {
    AppTheme {
        SearchConditionCheckboxItemCard(
            title = "紫",
            isSelected = true,
            onChecked = { },
            onClickLink = { },
        )
    }
}

@Preview
@Composable
fun SearchConditionCheckboxItemCardSizePreview() {
    AppTheme {
        SearchConditionCheckboxItemCard(
            title = "おもちゃ",
            description = "たのしい",
            isSelected = true,
            onChecked = { },
            onClickLink = { },
        )
    }
}
