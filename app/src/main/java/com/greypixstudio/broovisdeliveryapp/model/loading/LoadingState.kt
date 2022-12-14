package com.greypixstudio.broovisdeliveryapp.model.loading

import com.greypixstudio.broovisdeliveryapp.utils.api.ErrorData


data class LoadingState private constructor(val status: Status, val errorData: ErrorData? = null) {
    companion object {
        val LOADED = LoadingState(Status.SUCCESS)
        val LOADING = LoadingState(Status.RUNNING)
        fun error(errorData: ErrorData) = LoadingState(Status.FAILED, errorData)
    }

    enum class Status {
        RUNNING,
        SUCCESS,
        FAILED
    }
}