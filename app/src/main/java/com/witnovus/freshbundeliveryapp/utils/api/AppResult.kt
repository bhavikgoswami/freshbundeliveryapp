package com.witnovus.freshbundeliveryapp.utils.api

sealed class AppResult<out T> {
    data class Success<out T>(val successData : T) : AppResult<T>()
    data class Error<out T>(val errorData: ErrorData) : AppResult<T>()
}