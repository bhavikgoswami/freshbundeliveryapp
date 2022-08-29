package com.witnovus.freshbundeliveryapp.model.orderdata.notdeliveredorder


import com.google.gson.annotations.SerializedName

data class OrderNotDeliveredResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("error")
    val error: List<Any>,
    @SerializedName("message")
    val message: String,
    @SerializedName("results")
    val results: Any,
    @SerializedName("success")
    val success: Boolean
)