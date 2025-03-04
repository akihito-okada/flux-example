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
fun InformationAlertDialog(
    message: AlertDialogMessage,
    onClickPositive: () -> Unit,
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
                onClick = onClickPositive,
            ) {
                Text(
                    text = stringResource(id = message.positiveTextRestId),
                    style = AppTheme.typography.SubtitleMedium14L16,
                    color = AppTheme.colors.onAttentionInfo,
                )
            }
        },
    )
}

@Preview
@Composable
fun PreviewInformationAlertDialog() {
    AppTheme {
        InformationAlertDialog(
            message = AlertDialogMessage.Unknown,
            onClickPositive = {},
            onDismiss = {},
        )
    }
}

@Preview
@Composable
fun PreviewEmptyMessageAlertDialog() {
    AppTheme {
        InformationAlertDialog(
            message = AlertDialogMessage.Unknown,
            onClickPositive = {},
            onDismiss = {},
        )
    }
}
