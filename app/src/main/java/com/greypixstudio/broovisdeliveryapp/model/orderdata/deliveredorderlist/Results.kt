package com.greypixstudio.broovisdeliveryapp.model.orderdata.deliveredorderlist


import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("records")
    val records: List<Record>
)