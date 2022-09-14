package com.greypixstudio.broovisdeliveryapp.model.uploaddocument.documentlistdetail


import com.google.gson.annotations.SerializedName

data class Records(
    @SerializedName("aadhaar_Card")
    val aadhaarCard: String,
    @SerializedName("licence")
    val licence: String,
    @SerializedName("pan_card")
    val panCard: String
)