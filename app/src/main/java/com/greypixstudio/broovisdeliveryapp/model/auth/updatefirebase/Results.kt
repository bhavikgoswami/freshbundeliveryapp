package com.greypixstudio.broovisdeliveryapp.model.auth.updatefirebase


import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("user")
    val user: User
)