package com.witnovus.freshbundeliveryapp.model.auth.signup


import com.google.gson.annotations.SerializedName
import com.witnovus.freshbundeliveryapp.model.auth.user.User

data class Results(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: User
)