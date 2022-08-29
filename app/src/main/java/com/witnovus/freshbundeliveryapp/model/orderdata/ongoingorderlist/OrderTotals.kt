package com.witnovus.freshbundeliveryapp.model.orderdata.ongoingorderlist


import com.google.gson.annotations.SerializedName

data class OrderTotals(
    @SerializedName("delivered_orders")
    val deliveredOrders: Int,
    @SerializedName("new_subscribers")
    val newSubscribers: Int,
    @SerializedName("not_delivered_orders")
    val notDeliveredOrders: Int,
    @SerializedName("total_orders")
    val totalOrders: Int
)