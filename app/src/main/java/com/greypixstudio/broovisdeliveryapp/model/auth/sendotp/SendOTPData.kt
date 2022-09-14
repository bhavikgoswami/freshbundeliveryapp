package com.greypixstudio.broovisdeliveryapp.model.auth.sendotp


import com.google.gson.annotations.SerializedName

data class SendOTPData(
    @SerializedName("type")
    val type: Int
)