package com.kdy_soft.jetnewsclone.util

import android.content.res.Resources
import androidx.annotation.StringRes


sealed class ErrorMessage {
    abstract val id: Long

    data class ResourceMessage(@StringRes val resId: Int, override val id: Long) : ErrorMessage()
    data class StringMessage(val message: String, override val id: Long) : ErrorMessage()

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
