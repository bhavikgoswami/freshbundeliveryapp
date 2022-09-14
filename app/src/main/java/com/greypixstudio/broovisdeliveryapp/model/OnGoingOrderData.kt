package com.greypixstudio.broovisdeliveryapp.model

data class OnGoingOrderData(
    val image: String,
    val customerName: String,
    val orderId: String,
    val deliveryTime: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)