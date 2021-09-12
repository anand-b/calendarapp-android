package com.nandboolean.calendarapp.data.common

sealed class DataResult<out T> {
    data class Success<out T>(val data: T): DataResult<T>()
    data class Error(val error: Throwable): DataResult<Nothing>()
    data class ProceedWithWarning<out T>(val data: T, val warning: String): DataResult<T>()
}
