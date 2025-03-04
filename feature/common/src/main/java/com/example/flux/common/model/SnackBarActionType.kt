package com.example.flux.common.model

import android.content.res.Resources
import com.example.flux.model.R

enum class SnackBarActionType {
    None, UserDetailActivities, RemindMarketingEmailSettings, OK;

    val isNone get() = this == None

    fun actionName(resources: Resources): String = when (this) {
        None -> resources.getString(R.string.blank)
        UserDetailActivities -> resources.getString(R.string.open_setting_app)
        OK -> resources.getString(R.string.ok)
        RemindMarketingEmailSettings -> resources.getString(R.string.snackbar_setting_check)
    }
}
