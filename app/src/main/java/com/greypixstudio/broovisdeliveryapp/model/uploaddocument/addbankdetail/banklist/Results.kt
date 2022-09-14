package com.greypixstudio.broovisdeliveryapp.model.uploaddocument.addbankdetail.banklist


import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("records")
    val records: List<Record>
)