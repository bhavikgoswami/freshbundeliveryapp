package com.greypixstudio.broovisdeliveryapp.model.orderdata.ongoingorderlist


import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("order_totals")
    val orderTotals: OrderTotals,
    @SerializedName("orders")
    val orders: List<Order>
)