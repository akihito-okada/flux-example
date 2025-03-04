package com.example.flux.remote.request.toys

import com.google.gson.annotations.SerializedName

// おもちゃ検索リクエスト
data class SearchToysRequest(
    @SerializedName("ids") val ids: List<Int>?,
    @SerializedName("material_ids") val materialIds: List<Int>?,
    @SerializedName("color_ids") val colorIds: List<Int>?,
    @SerializedName("price_min") val priceMin: Int?,
    @SerializedName("price_max") val priceMax: Int?,
    @SerializedName("created_years") val createdYears: List<Int>?,
    @SerializedName("keyword") val keyword: String?,
    @SerializedName("category_ids") val categoryIds: List<Int>?,
    @SerializedName("request_user_id") val userId: String?,
    @SerializedName("order_by") val orderBy: String?,
)
