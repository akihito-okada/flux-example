package com.example.flux.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.flux.model.AppThemeType
import com.example.flux.model.UserId

private const val USER_PREFERENCES_KEY = "user_preferences_key"
private const val KEY_SHOULD_SHOW_RECOMPOSE_HIGHLIGHTER = "should_show_recompose_highlighter"
private const val KEY_USER_ID = "key_user_id"
private const val KEY_APP_THEME = "app_theme"
private const val VALUE_DUMMY_USER_ID = "302338e1-ed6a-4352-8102-9aeb914e7ad7"

class UserPreferences private constructor(
    context: Context,
) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(USER_PREFERENCES_KEY, Context.MODE_PRIVATE)

    var userId: UserId
        get() = UserId.fromValue(sharedPreferences.getString(KEY_USER_ID, VALUE_DUMMY_USER_ID))
        set(userId) {
            sharedPreferences.edit {
                putString(KEY_USER_ID, userId.value)
            }
        }

    var shouldShowRecomposeHighlighter: Boolean
        get() = sharedPreferences.getBoolean(KEY_SHOULD_SHOW_RECOMPOSE_HIGHLIGHTER, false)
        set(shouldShowReComposeHighlighter) {
            sharedPreferences.edit {
                putBoolean(KEY_SHOULD_SHOW_RECOMPOSE_HIGHLIGHTER, shouldShowReComposeHighlighter)
            }
        }

    fun setAppTheme(appTheme: AppThemeType) {
        sharedPreferences.edit {
            putInt(KEY_APP_THEME, appTheme.ordinal)
        }
    }

    fun loadAppTheme(): AppThemeType {
        return AppThemeType.fromOrdinal(sharedPreferences.getInt(KEY_APP_THEME, AppThemeType.default.ordinal))
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: UserPreferences? = null

        @JvmStatic
        fun getInstance(
            context: Context,
        ): UserPreferences =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildInstance(
                        context,
                    ).also { INSTANCE = it }
            }

        private fun buildInstance(
            context: Context,
        ): UserPreferences {
            return UserPreferences(
                context,
            )
        }
    }
}
