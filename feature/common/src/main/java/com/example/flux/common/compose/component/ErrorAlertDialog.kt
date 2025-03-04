package com.example.flux.common.compose.component

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.flux.common.compose.theme.AppTheme
import com.example.flux.common.model.AlertDialogMessage

@Composable
fun ErrorAlertDialog(
    message: AlertDialogMessage,
    onClickPositive: () -> Unit,
    onClickNegative: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss.invoke()
        },
        title = {
            if (message.isTitleEmpty().not()) {
                Text(
                    text = stringResource(id = message.titleResId),
                    style = AppTheme.typography.SubtitleBold13L20,
                )
            }
        },
        text = {
            if (message.isMessageEmpty().not()) {
                Text(
                    text = stringResource(id = message.messageResId),
                    style = AppTheme.typography.BodyRegular13L20,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onClickPositive.invoke()
                },
            ) {
                Text(
                    text = stringResource(id = message.positiveTextRestId),
                    style = AppTheme.typography.SubtitleMedium14L16,
                    color = AppTheme.colors.onErrorPrimary,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onClickNegative.invoke()
                },
            ) {
                Text(
                    text = stringResource(id = message.negativeTextResId),
                    style = AppTheme.typography.SubtitleMedium14L16,
                    color = AppTheme.colors.onErrorPrimary,
                )
            }
        },
    )
}

@Preview
@Composable
fun PreviewErrorAlertDialog() {
    AppTheme {
        ErrorAlertDialog(
            message = AlertDialogMessage.Unknown,
            {
                // no-op
            },
            {
                // no-op
            },
            {
                // no-op
            },
        )
    }
}
