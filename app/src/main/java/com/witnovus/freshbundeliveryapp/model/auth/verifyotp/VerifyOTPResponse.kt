package com.witnovus.freshbundeliveryapp.model.auth.verifyotp


import com.google.gson.annotations.SerializedName
import com.witnovus.freshbundeliveryapp.model.auth.signup.Results

data class VerifyOTPResponse(
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