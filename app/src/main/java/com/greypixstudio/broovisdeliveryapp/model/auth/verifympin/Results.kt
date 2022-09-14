package com.greypixstudio.broovisdeliveryapp.model.auth.verifympin


import com.google.gson.annotations.SerializedName
import com.greypixstudio.broovisdeliveryapp.model.auth.user.User

data class Results(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: User
)