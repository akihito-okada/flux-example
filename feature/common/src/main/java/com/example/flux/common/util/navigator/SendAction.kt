package com.example.flux.common.util.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import timber.log.Timber

private const val CONTENT_TYPE_TEXT_PLAIN = "text/plain"
private const val TWITTER_TWEET_URL = "https://twitter.com/intent/tweet?text="
private const val LINE_MSG_URL = "https://line.me/R/msg/text/?"
private const val FACEBOOK_SHARE_URL_PREFIX = "https://www.facebook.com/share.php?u="
private const val FACEBOOK_SHARE_URL_QUOTE = "&quote="

class SendAction {
    companion object {
        @JvmStatic
        fun createIntent(uri: Uri): Intent {
            return Intent(Intent.ACTION_VIEW, uri)
        }

        @JvmStatic
        fun createIntentChooser(message: String): Intent {
            return Intent(Intent.ACTION_SEND).also {
                it.type = CONTENT_TYPE_TEXT_PLAIN
                it.putExtra(Intent.EXTRA_TEXT, message)
            }
        }

        @JvmStatic
        fun createIntentTwitterShare(message: String): Intent {
            val url = getTwitterSchemaUrl(message)
            return Intent(Intent.ACTION_VIEW, Uri.parse(url)).also {
                it.setDataAndType(Uri.parse(url), CONTENT_TYPE_TEXT_PLAIN)
            }
        }

        @JvmStatic
        fun createIntentLineShare(message: String): Intent {
            val url = getLineSchemaUrl(message)
            return Intent(Intent.ACTION_VIEW, Uri.parse(url)).also {
                it.setDataAndType(Uri.parse(url), CONTENT_TYPE_TEXT_PLAIN)
            }
        }

        @JvmStatic
        private fun getTwitterSchemaUrl(message: String): String {
            return try {
                val text = Uri.encode(message)
                val share = "$TWITTER_TWEET_URL$text"
                share
            } catch (exception: Exception) {
                Timber.e(exception)
                TWITTER_TWEET_URL
            }
        }

        @JvmStatic
        fun getLineSchemaUrl(message: String): String {
            return try {
                val text = Uri.encode(message)
                "$LINE_MSG_URL$text"
            } catch (exception: Exception) {
                Timber.e(exception.message)
                ""
            }
        }

        @JvmStatic
        fun createIntentApplicationDetailSetting(context: Context): Intent {
            return Intent().also {
                it.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                it.addCategory(Intent.CATEGORY_DEFAULT)
                it.data = Uri.parse("package:${context.packageName}")
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                it.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                it.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }
        }
    }
}
