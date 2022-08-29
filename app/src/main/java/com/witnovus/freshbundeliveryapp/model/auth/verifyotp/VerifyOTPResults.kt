package com.witnovus.freshbundeliveryapp.model.auth.verifyotp


import com.google.gson.annotations.SerializedName
import com.witnovus.freshbundeliveryapp.model.auth.user.User

data class VerifyOTPResults(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: User
)