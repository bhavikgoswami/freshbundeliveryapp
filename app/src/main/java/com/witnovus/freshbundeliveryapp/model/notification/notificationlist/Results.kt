package com.witnovus.freshbundeliveryapp.model.notification.notificationlist


import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("records")
    val records: List<Record>
)