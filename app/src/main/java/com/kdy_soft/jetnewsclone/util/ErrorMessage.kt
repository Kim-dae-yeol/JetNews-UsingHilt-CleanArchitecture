package com.kdy_soft.jetnewsclone.util

import android.content.res.Resources
import androidx.annotation.StringRes


sealed class ErrorMessage {
    data class ResourceMessage(val id: Long, @StringRes val resId: Int) : ErrorMessage()
    data class StringMessage(val id: Long, val message: String) : ErrorMessage()

    fun getMessageString(res: Resources): String {
        return when (this) {
            is ResourceMessage -> {
                res.getString(resId)
            }

            is StringMessage -> {
                message
            }
        }
    }
}
