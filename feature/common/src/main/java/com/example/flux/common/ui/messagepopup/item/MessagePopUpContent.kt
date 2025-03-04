package com.example.flux.common.ui.messagepopup.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flux.common.R
import com.example.flux.common.compose.theme.AppTheme
import com.example.flux.common.model.DialogMessage

@Composable
fun MessagePopUpContent(
    dialogMessage: DialogMessage,
    onClickPositive: () -> Unit = {},
    onClickNegative: () -> Unit = {},
    onClickClose: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 38.dp),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 4.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClickClose,
                    ),
            ) {
                Image(
                    modifier = Modifier.padding(8.dp),
                    painter = painterResource(id = R.drawable.ic_close_white_18dp),
                    contentDescription = stringResource(id = R.string.close),
                )
            }
            Box(
                modifier = Modifier
                    .width(316.dp)
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(size = 8.dp))
                    .background(AppTheme.colors.surfaceDialog),
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                ) {
                    val title = when {
                        dialogMessage.isSetResId -> stringResource(id = dialogMessage.titleResId)
                        else -> dialogMessage.titleString
                    }
                    if (title.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 20.dp,
                                ),
                            text = title,
                            color = AppTheme.colors.onSurfaceDark,
                            style = AppTheme.typography.SubtitleMedium18L26,
                            textAlign = TextAlign.Center,
                        )
                    }
                    val message = when {
                        dialogMessage.isSetResId -> stringResource(id = dialogMessage.messageResId)
                        else -> dialogMessage.messageString
                    }
                    if (message.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 20.dp,
                                ),
                            text = message,
                            color = AppTheme.colors.onSurfaceDark,
                            style = AppTheme.typography.BodyRegular13L20,
                            textAlign = TextAlign.Center,
                        )
                    }
                    if (dialogMessage.hasPositiveButton) {
                        Box(
                            modifier = Modifier
                                .padding(
                                    top = 24.dp,
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onClickPositive,
                                )
                                .clip(RoundedCornerShape(50))
                                .background(AppTheme.colors.surfaceDark),
                        ) {
                            Row(
                                Modifier
                                    .align(Alignment.Center)
                                    .height(44.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically),
                                    text = stringResource(dialogMessage.positiveTextRestId),
                                    color = AppTheme.colors.onBackgroundBright,
                                    style = AppTheme.typography.SubtitleMedium16L24,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                    if (dialogMessage.hasNegativeButton) {
                        Box(
                            modifier = Modifier
                                .padding(
                                    top = 12.dp,
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onClickNegative,
                                )
                                .clip(RoundedCornerShape(50))
                                .background(AppTheme.colors.surfaceBright)
                                .border(width = 1.dp, shape = RoundedCornerShape(50), color = AppTheme.colors.borderMiddle),
                        ) {
                            Row(
                                Modifier
                                    .align(Alignment.Center)
                                    .height(44.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically),
                                    text = stringResource(dialogMessage.negativeTextRestId),
                                    color = AppTheme.colors.onSurfaceDark,
                                    style = AppTheme.typography.SubtitleMedium16L24,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewNetworkError() {
    AppTheme {
        MessagePopUpContent(DialogMessage.NetworkError)
    }
}
