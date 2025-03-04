package com.example.flux.repository.impl

import com.example.flux.model.AppThemeType
import com.example.flux.model.User
import com.example.flux.model.UserId
import com.example.flux.model.util.CoroutinePlugin
import com.example.flux.preferences.UserPreferences
import com.example.flux.remote.ToysFactoryApiService
import com.example.flux.remote.request.toys.CreateUserRequest
import com.example.flux.repository.UserRepository
import kotlinx.coroutines.withContext
import java.util.UUID

class UserRepositoryImpl(
    private val toysFactoryApiService: ToysFactoryApiService,
    private val userPreferences: UserPreferences,
) : UserRepository {

    override suspend fun createUser(): User {
        return withContext(CoroutinePlugin.ioDispatcher) {
            val email = "${UUID.randomUUID()}@example.com"
            toysFactoryApiService.createUser(CreateUserRequest(email))
                .first()
                .convert().also { user ->
                    userPreferences.userId = user.id
                }
        }
    }

    override fun setAppTheme(appTheme: AppThemeType) {
        userPreferences.setAppTheme(appTheme)
    }

    override fun loadShouldShowRecomposeHighlighter(): Boolean {
        return userPreferences.shouldShowRecomposeHighlighter
    }

    override fun loadAppTheme(): AppThemeType {
        return userPreferences.loadAppTheme()
    }

    override fun getUserId(): UserId {
        return userPreferences.userId
    }

    override fun hasUserId(): Boolean {
        return userPreferences.userId.isValid
    }
}
