package com.greypixstudio.broovisdeliveryapp.utils.api


import com.google.gson.annotations.SerializedName

data class APIResponse(
    @SerializedName("data")
    val data: Any?,
    @SerializedName("error_code")
    val errorCode: Int,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean
)