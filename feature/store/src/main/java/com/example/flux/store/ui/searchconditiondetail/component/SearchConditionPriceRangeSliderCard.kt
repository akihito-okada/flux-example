package com.example.flux.store.ui.searchconditiondetail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RangeSlider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flux.common.compose.ext.recomposeHighlighter
import com.example.flux.common.compose.theme.AppTheme
import com.example.flux.model.Price
import com.example.flux.model.PriceRange
import com.example.flux.model.R

@Composable
fun SearchConditionPriceRangeSliderCard(
    modifier: Modifier = Modifier,
    shouldShowRecomposeHighlighter: Boolean = false,
    selectedPriceRange: PriceRange,
    onValueChangeFinished: (PriceRange) -> Unit = {},
) {
    val range = remember(selectedPriceRange.revision) {
        mutableStateOf(
            selectedPriceRange,
        )
    }
    val minPrice = range.value.minPrice
    val maxPrice = range.value.maxPrice
    Column(
        modifier = modifier
            .recomposeHighlighter(shouldShowRecomposeHighlighter)
            .padding(
                horizontal = 16.dp,
            )
            .fillMaxWidth(),
    ) {
        Row(modifier = Modifier.padding(vertical = 12.dp)) {
            Text(
                text = stringResource(R.string.price_format, minPrice.value),
                color = AppTheme.colors.onSurfaceDark,
                style = AppTheme.typography.BodyRegular14L22,
            )
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = stringResource(id = R.string.price_range_divider),
                color = AppTheme.colors.onSurfaceDark,
                style = AppTheme.typography.BodyRegular14L22,
            )
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = if (maxPrice.isRangeMax) {
                    stringResource(R.string.store_index_refine_search_nolimit)
                } else {
                    stringResource(R.string.price_format, maxPrice.value)
                },
                color = AppTheme.colors.onSurfaceDark,
                style = AppTheme.typography.BodyRegular14L22,
            )
        }
        RangeSlider(
            modifier = Modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 24.dp,
                ),
            value = range.value.minPrice.sliderValue..range.value.maxPrice.sliderValue,
            onValueChange = {
                range.value = selectedPriceRange.copy(
                    minPrice = Price(it.start.toInt() * Price.UNIT_SLIDER_PRICE),
                    maxPrice = Price(it.endInclusive.toInt() * Price.UNIT_SLIDER_PRICE),
                )
                onValueChangeFinished.invoke(range.value)
            },
            colors = SliderDefaults.colors(
                thumbColor = AppTheme.colors.onAttentionInfo,
                activeTrackColor = AppTheme.colors.onAttentionInfo,
            ),
            valueRange = 0.0f..100f,
        )
    }
}

@Preview
@Composable
fun SearchConditionPriceRangeSliderCardPreview() {
    AppTheme {
        SearchConditionPriceRangeSliderCard(selectedPriceRange = PriceRange()) {}
    }
}
