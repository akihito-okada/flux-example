package com.example.flux.remote

import com.example.flux.remote.request.toys.CreateUserRequest
import com.example.flux.remote.request.toys.SaveToyRequest
import com.example.flux.remote.request.toys.SearchToysCountRequest
import com.example.flux.remote.request.toys.SearchToysRequest
import com.example.flux.remote.response.UserResponse
import com.example.flux.remote.response.toys.CountResponse
import com.example.flux.remote.response.toys.ToyCategoryResponse
import com.example.flux.remote.response.toys.ToyColorResponse
import com.example.flux.remote.response.toys.ToyMaterialResponse
import com.example.flux.remote.response.toys.ToyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ToysFactoryApiService {

    // ユーザー関連のエンドポイント
    @POST("users")
    @Headers("Prefer: return=representation")
    suspend fun createUser(@Body createUserRequest: CreateUserRequest): List<UserResponse>

    // おもちゃ検索エンドポイント
    @POST("rpc/search_toys")
    suspend fun searchToys(@Body searchToysRequest: SearchToysRequest): List<ToyResponse>

    // おもちゃの検索結果の件数取得
    @POST("rpc/search_toys_count")
    suspend fun searchToysCount(@Body searchToysCountRequest: SearchToysCountRequest): CountResponse

    // おもちゃの保存
    @POST("user_saved_toys")
    suspend fun saveToy(@Body saveToyRequest: SaveToyRequest)

    // おもちゃの保存削除
    @DELETE("user_saved_toys")
    suspend fun removeSavedToy(
        @Query("toy_id") toyId: String,  // "eq.1" の形で送信
        @Query("user_id") userId: String  // "eq.0054a70c-b5c5-49b1-8d07-770329629557" の形で送信
    ): Response<Void>

    // カテゴリ一覧取得
    @GET("categories")
    suspend fun getCategories(): List<ToyCategoryResponse>

    // 色一覧取得
    @GET("colors")
    suspend fun getColors(): List<ToyColorResponse>

    // 素材一覧取得
    @GET("materials")
    suspend fun getMaterials(): List<ToyMaterialResponse>
}
