package com.witnovus.freshbundeliveryapp.model.orderdata.deliveredorder


import com.google.gson.annotations.SerializedName

data class Count(
    @SerializedName("records")
    val records: String
)