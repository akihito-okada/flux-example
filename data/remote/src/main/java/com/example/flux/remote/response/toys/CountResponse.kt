package com.example.flux.remote.response.toys

import com.example.flux.model.ToysCount
import com.google.gson.annotations.SerializedName

// おもちゃの検索結果のカウントレスポンス
data class CountResponse(
    @SerializedName("count") val count: Int,
) {

    fun convert(): ToysCount {
        return ToysCount(
            count = count,
        )
    }
}

