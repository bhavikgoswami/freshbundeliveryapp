package com.greypixstudio.broovisdeliveryapp.model.orderdata.deliveredorder


import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("records")
    val records: Records
)