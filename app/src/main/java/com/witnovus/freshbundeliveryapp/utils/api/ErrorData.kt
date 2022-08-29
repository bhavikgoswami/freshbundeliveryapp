package com.witnovus.freshbundeliveryapp.utils.api


import com.google.gson.annotations.SerializedName

data class ErrorData(
    @SerializedName("data")
    val data: Any?,
    @SerializedName("code")
    val errorCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean,

    )