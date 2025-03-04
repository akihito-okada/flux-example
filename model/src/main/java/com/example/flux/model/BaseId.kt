package com.example.flux.model

import android.os.Parcelable

abstract class BaseId(open val value: Long) : Parcelable {
    open val isValid: Boolean get() = value != INVALID_ID

    override fun toString(): String {
        return value.toString()
    }

    companion object {

        const val INVALID_ID = 0L
    }
}
