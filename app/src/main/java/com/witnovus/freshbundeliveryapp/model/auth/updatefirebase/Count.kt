package com.witnovus.freshbundeliveryapp.model.auth.updatefirebase


import com.google.gson.annotations.SerializedName

data class Count(
    @SerializedName("user")
    val user: String
)