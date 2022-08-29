package com.witnovus.freshbundeliveryapp.model.auth.setmpin


import com.google.gson.annotations.SerializedName
import com.witnovus.freshbundeliveryapp.model.auth.user.User

data class Results(
    @SerializedName("user")
    val user: User
)