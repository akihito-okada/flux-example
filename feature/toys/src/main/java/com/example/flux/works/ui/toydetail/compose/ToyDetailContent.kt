package com.example.flux.works.ui.toydetail.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flux.common.compose.theme.AppTheme
import com.example.flux.model.Toy
import com.example.flux.model.ToyCategory
import com.example.flux.model.ToyCategoryId
import com.example.flux.model.ToyColor
import com.example.flux.model.ToyColorId
import com.example.flux.model.ToyId
import com.example.flux.model.ToyMaterial
import com.example.flux.model.ToyMaterialId
import com.example.flux.works.R
import coil.compose.AsyncImage

@Composable
fun ToyDetailContent(
    modifier: Modifier = Modifier,
    uiState: ToyDetailScreenContent,
    onSaveClick: (Boolean) -> Unit,
) {
    val toy = uiState.toy
    var hasSaved by remember { mutableStateOf(toy.hasSaved) }
    LaunchedEffect(uiState.toySaveRequest) {
        hasSaved = uiState.toy.hasSaved
    }
    LazyColumn(
        modifier = modifier
            .background(AppTheme.colors.surfaceBright)
            .fillMaxSize(),
    ) {
        // 画像
        item {
            AsyncImage(
                model = toy.image,
                contentDescription = toy.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
            )
        }

        // メイン情報
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // タイトル
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = toy.name,
                        fontSize = 24.sp,
                        color = AppTheme.colors.onSurfaceDark,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    IconButton(
                        modifier = Modifier
                            .clip(CircleShape),
                        onClick = {
                            onSaveClick.invoke(!hasSaved)
                        },
                    ) {
                        Image(
                            modifier = Modifier.size(32.dp),
                            painter = painterResource(
                                when (hasSaved) {
                                    true -> R.drawable.ic_keep_on_black_48dp
                                    false -> R.drawable.ic_keep_off_white_48dp
                                },
                            ),
                            contentDescription = stringResource(R.string.save),
                        )
                    }
                }

                // メーカー名
                Text(
                    modifier = Modifier.padding(bottom = 12.dp),
                    text = toy.makerName,
                    style = AppTheme.typography.BodyRegular14L22,
                    color = AppTheme.colors.onSurfaceDark,
                )

                // 価格
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = stringResource(R.string.toy_price_format, toy.price),
                    style = AppTheme.typography.HeadlineBold16L24,
                    color = AppTheme.colors.onSurfaceDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                // 説明
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = toy.description,
                    style = AppTheme.typography.BodyRegular14L22,
                    color = AppTheme.colors.onSurfaceDark,
                )

                // 詳細情報
                Text(
                    text = "カテゴリ: ${toy.category.name}",
                    style = AppTheme.typography.CaptionRegular10L13,
                    color = AppTheme.colors.onSurfaceDeep,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
                Text(
                    text = "サイズ: ${toy.dimensions}",
                    style = AppTheme.typography.CaptionRegular10L13,
                    color = AppTheme.colors.onSurfaceDeep,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
                Text(
                    text = "素材: ${toy.materials.joinToString(", ") { it.name }}",
                    style = AppTheme.typography.CaptionRegular10L13,
                    color = AppTheme.colors.onSurfaceDeep,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
                Text(
                    text = "色: ${toy.colors.joinToString(", ") { it.name }}",
                    style = AppTheme.typography.CaptionRegular10L13,
                    color = AppTheme.colors.onSurfaceDeep,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
                Text(
                    text = "作成年: ${toy.workCreatedAt}",
                    style = AppTheme.typography.CaptionRegular10L13,
                    color = AppTheme.colors.onSurfaceDeep,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }
        }
    }
}

// 使用
@Preview
@Composable
private fun ToyDetailContentPreview() {
    val toy = Toy(
        id = ToyId(1),
        name = "ルーレット",
        image = "https://example.com/toy.jpg",
        description = "ルーレットは電子玩具に分類されるおもちゃで、布で作られています。",
        hasSaved = false,
        category = ToyCategory(ToyCategoryId(1), "電子玩具"),
        materials = listOf(ToyMaterial(ToyMaterialId(1), "布")),
        dimensions = "111 x 11 x 1",
        price = 6252,
        colors = listOf(ToyColor(ToyColorId(1), "赤")),
        workCreatedAt = 2013,
        createdAt = "2013/12/11",
        publishedAt = "2013/12/11",
        makerName = "中村骨董品店",
    )
    AppTheme {
        ToyDetailContent(
            uiState = ToyDetailScreenContent(
                toy = toy,
            ),
            onSaveClick = { /* 保存ロジック */ },
        )
    }
}
