package com.example.flux.common.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flux.common.R
import com.example.flux.common.compose.theme.AppTheme

@Composable
fun ErrorItem(
    modifier: Modifier = Modifier,
    messageRes: Int = R.string.general_error,
    onClickRetry: () -> Unit,
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(messageRes),
            maxLines = 1,
            modifier = Modifier.weight(1f),
            style = AppTheme.typography.HeadlineMedium18L26,
            color = Color.Red,
        )
        OutlinedButton(onClick = onClickRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Preview
@Composable
fun ErrorItemPreview() {
    AppTheme {
        ErrorItem {}
    }
}
