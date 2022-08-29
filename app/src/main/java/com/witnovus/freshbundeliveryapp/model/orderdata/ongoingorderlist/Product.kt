package com.witnovus.freshbundeliveryapp.model.orderdata.ongoingorderlist


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("product_image")
    val productImage: String,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("qty")
    val qty: Int,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("unit_value")
    val unitValue: Int
)