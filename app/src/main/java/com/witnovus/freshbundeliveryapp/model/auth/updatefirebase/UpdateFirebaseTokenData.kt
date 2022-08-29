package com.witnovus.freshbundeliveryapp.model.auth.updatefirebase


import com.google.gson.annotations.SerializedName

data class UpdateFirebaseTokenData(
    @SerializedName("code")
    val code: Int,
    @SerializedName("count")
    val count: Count,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("results")
    val results: Results,
    @SerializedName("success")
    val success: Boolean
)