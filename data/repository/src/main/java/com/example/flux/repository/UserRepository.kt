package com.example.flux.repository

import com.example.flux.model.AppThemeType
import com.example.flux.model.User
import com.example.flux.model.UserId

interface UserRepository {
    suspend fun createUser(): User
    fun hasUserId(): Boolean
    fun getUserId(): UserId
    fun loadAppTheme(): AppThemeType
    fun setAppTheme(appTheme: AppThemeType)
    fun loadShouldShowRecomposeHighlighter(): Boolean
}
