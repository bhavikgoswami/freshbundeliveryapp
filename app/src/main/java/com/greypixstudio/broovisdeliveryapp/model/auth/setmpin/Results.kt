package com.greypixstudio.broovisdeliveryapp.model.auth.setmpin


import com.google.gson.annotations.SerializedName
import com.greypixstudio.broovisdeliveryapp.model.auth.user.User

data class Results(
    @SerializedName("user")
    val user: User
)