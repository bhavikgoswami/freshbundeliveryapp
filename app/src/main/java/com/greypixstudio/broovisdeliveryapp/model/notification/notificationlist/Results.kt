package com.greypixstudio.broovisdeliveryapp.model.notification.notificationlist


import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("records")
    val records: List<Record>
)