package com.witnovus.freshbundeliveryapp.model.orderdata.ongoingorderlist


import com.google.gson.annotations.SerializedName

data class Count(
    @SerializedName("order_totals")
    val orderTotals: Int,
    @SerializedName("orders")
    val orders: Int
)