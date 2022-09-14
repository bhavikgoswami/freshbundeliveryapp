package com.greypixstudio.broovisdeliveryapp.model.orderdata.deliveredorderlist


import com.google.gson.annotations.SerializedName

data class Count(
    @SerializedName("records")
    val records: Int
)