package com.greypixstudio.broovisdeliveryapp.model.auth.setmpin


import com.google.gson.annotations.SerializedName

data class SetMpinResponse(
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