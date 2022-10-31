package com.greypixstudio.broovisdeliveryapp.model.orderdata.paymentRecievedData


import com.google.gson.annotations.SerializedName

data class Count(
    @SerializedName("records")
    val records: String
)