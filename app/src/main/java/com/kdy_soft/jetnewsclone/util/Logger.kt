package com.kdy_soft.jetnewsclone.util

import android.annotation.SuppressLint
import android.util.Log

@SuppressWarnings("unused")
object Logger {
    private const val FILE_MAX_LENGTH = 20
    private fun getTag(): String =
        "${Thread.currentThread().stackTrace[4].fileName.take(FILE_MAX_LENGTH)}:${Thread.currentThread().stackTrace[4].lineNumber}"

    fun d(msg: String) {
        Log.d(getTag(), msg)
    }

    fun e(msg: String) {
        Log.e(getTag(), msg)
    }

    fun i(msg: String) {
        Log.i(getTag(), msg)
    }
}