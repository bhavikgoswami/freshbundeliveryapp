package com.witnovus.freshbundeliveryapp.model.auth.logout


import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("results")
    val results: Any,
    @SerializedName("success")
    val success: Boolean
)