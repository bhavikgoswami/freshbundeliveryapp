package com.witnovus.freshbundeliveryapp.model.auth.verifyotp


import com.google.gson.annotations.SerializedName

data class Count(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: String
)