package com.example.flux.common.compose.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flux.common.compose.theme.AppTheme
import com.example.flux.common.model.EmptyViewType

@Composable
fun EmptyItem(
    type: EmptyViewType,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        if (type.hasHeaderPaddingSmall) {
            Spacer(modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp))
        }
        if (type.hasHeaderPadding) {
            Spacer(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp))
        }
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = stringResource(type.titleRes),
            style = AppTheme.typography.HeadlineMedium24L32,
        )
        if (type.hasMessage) {
            Text(
                text = stringResource(type.messageRes),
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                style = AppTheme.typography.BodyRegular14L22,
            )
        }
        if (type.hasNavigationButton) {
            TextButton(
                onClick = onClick,
                modifier = Modifier.padding(top = 32.dp, start = 8.dp, end = 8.dp),
            ) {
                Text(
                    text = stringResource(type.navigateActionRes),
                    style = AppTheme.typography.SubtitleMedium14L22,
                    color = AppTheme.colors.onAttentionSuccessVariant,
                )
            }
        }
    }
}

@Preview
@Composable
fun EmptyItemPreview() {
    AppTheme {
        EmptyItem(type = EmptyViewType.PurchasableWorks) {}
    }
}
