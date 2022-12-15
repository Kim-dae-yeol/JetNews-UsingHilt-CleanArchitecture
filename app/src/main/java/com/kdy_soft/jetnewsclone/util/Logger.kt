package com.kdy_soft.jetnewsclone.util

import android.annotation.SuppressLint
import android.util.Log

@SuppressWarnings("unused")
object Logger {
    private fun getTag(): String =
        "${Thread.currentThread().stackTrace[4].className}.${Thread.currentThread().stackTrace[4].methodName}"

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