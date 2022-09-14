package com.greypixstudio.broovisdeliveryapp.model.auth.verifyotp


import com.google.gson.annotations.SerializedName
import com.greypixstudio.broovisdeliveryapp.model.auth.user.User

data class VerifyOTPResults(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: User
)