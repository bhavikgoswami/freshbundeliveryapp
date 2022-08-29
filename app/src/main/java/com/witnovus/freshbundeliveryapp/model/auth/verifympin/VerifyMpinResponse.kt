package com.witnovus.freshbundeliveryapp.model.auth.verifympin


import com.google.gson.annotations.SerializedName

data class VerifyMpinResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("count")
    val count: Count,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("results")
    val results: Results,
    @SerializedName("success")
    val success: Boolean
)