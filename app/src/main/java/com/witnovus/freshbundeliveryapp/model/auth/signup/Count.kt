package com.witnovus.freshbundeliveryapp.model.auth.signup


import com.google.gson.annotations.SerializedName

data class Count(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: String
)