package com.greypixstudio.broovisdeliveryapp.model.orderdata.paymentRecievedData


import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("records")
    val records: Records
)