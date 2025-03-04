package com.example.flux.common.compose.component

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flux.common.R
import com.example.flux.common.compose.theme.AppTheme

@Composable
fun TopAppBarPrimary(
    title: String = "",
    onClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = AppTheme.typography.HeadlineMedium18L26,
                color = AppTheme.colors.onSurfaceDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.return_back),
                    tint = AppTheme.colors.onSurfaceDark,
                )
            }
        },
        elevation = 0.dp,
    )
}

@Preview
@Composable
fun TopAppBarPrimaryPreview() {
    AppTheme {
        TopAppBarPrimary("おもちゃ") {
        }
    }
}
