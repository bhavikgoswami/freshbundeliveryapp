package com.greypixstudio.broovisdeliveryapp.model.notification.notificationlist


import com.google.gson.annotations.SerializedName

data class NotificationListResponse(
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