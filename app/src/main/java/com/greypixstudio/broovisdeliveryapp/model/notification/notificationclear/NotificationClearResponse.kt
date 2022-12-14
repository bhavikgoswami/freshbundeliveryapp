package com.greypixstudio.broovisdeliveryapp.model.notification.notificationclear


import com.google.gson.annotations.SerializedName

data class NotificationClearResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("results")
    val results: Any,
    @SerializedName("success")
    val success: Boolean
)