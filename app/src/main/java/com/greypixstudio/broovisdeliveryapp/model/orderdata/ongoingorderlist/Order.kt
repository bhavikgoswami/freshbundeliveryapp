package com.greypixstudio.broovisdeliveryapp.model.orderdata.ongoingorderlist


import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("address")
    val address: String,
    @SerializedName("apartment_name")
    val apartmentName: String,
    @SerializedName("building_type")
    val buildingType: String,
    @SerializedName("customer_address_id")
    val customerAddressId: Int,
    @SerializedName("customer_id")
    val customerId: Int,
    @SerializedName("customer_mobile")
    val customerMobile: String,
    @SerializedName("customer_name")
    val customerName: String,
    @SerializedName("delivery_status")
    val deliveryStatus: String,
    @SerializedName("delivery_time")
    val deliveryTime: String,
    @SerializedName("delivery_user_id")
    val deliveryUserId: Int,
    @SerializedName("flat_no")
    val flatNo: Int,
    @SerializedName("landmark")
    val landmark: String,
    @SerializedName("latitudes")
    val latitudes: String,
    @SerializedName("longitudes")
    val longitudes: String,
    @SerializedName("ORDER_NUMBER")
    val orderNumber: String,
    @SerializedName("order_id")
    val orderId: Int,
    @SerializedName("order_status")
    val orderStatus: String,
    @SerializedName("pending_qty")
    val pendingQty: String,
    @SerializedName("plany_type")
    val planyType: String,
    @SerializedName("plot_no")
    val plotNo: Any,
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("street_address")
    val streetAddress: Any,
    @SerializedName("time_title")
    val timeTitle: String,
    @SerializedName("total_qty")
    val totalQty: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("wing")
    val wing: String,
    @SerializedName("cash_payment")
    val cashPayment: String
)