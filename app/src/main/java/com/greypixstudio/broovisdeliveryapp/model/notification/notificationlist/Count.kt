package com.greypixstudio.broovisdeliveryapp.model.notification.notificationlist


import com.google.gson.annotations.SerializedName

data class Count(
    @SerializedName("records")
    val records: Int
)