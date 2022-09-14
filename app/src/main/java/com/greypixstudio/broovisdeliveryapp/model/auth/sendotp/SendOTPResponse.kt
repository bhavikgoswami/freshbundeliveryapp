package com.greypixstudio.broovisdeliveryapp.model.auth.sendotp


import com.google.gson.annotations.SerializedName

data class SendOTPResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("count")
    val count: Count,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("results")
    val results: SendOTPResults,
    @SerializedName("success")
    val success: Boolean
)