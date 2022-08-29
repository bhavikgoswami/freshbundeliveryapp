package com.witnovus.freshbundeliveryapp.model.orderdata.deliveredorder


import com.google.gson.annotations.SerializedName

data class Records(
    @SerializedName("cancelled_dt")
    val cancelledDt: Any,
    @SerializedName("core_delivery_user_id")
    val coreDeliveryUserId: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("created_by")
    val createdBy: Int,
    @SerializedName("customer_address")
    val customerAddress: String,
    @SerializedName("customer_address_id")
    val customerAddressId: Int,
    @SerializedName("customer_id")
    val customerId: Int,
    @SerializedName("delivery_charges")
    val deliveryCharges: String,
    @SerializedName("delivery_dt")
    val deliveryDt: String,
    @SerializedName("delivery_status")
    val deliveryStatus: String,
    @SerializedName("delivery_time_id")
    val deliveryTimeId: Int,
    @SerializedName("delivery_user_id")
    val deliveryUserId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("ORDER_NUMBER")
    val oRDERNUMBER: String,
    @SerializedName("order_dt")
    val orderDt: String,
    @SerializedName("order_no")
    val orderNo: Int,
    @SerializedName("order_status")
    val orderStatus: String,
    @SerializedName("payment_by")
    val paymentBy: String,
    @SerializedName("payment_id")
    val paymentId: Any,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("store_id")
    val storeId: Int,
    @SerializedName("subscription_order")
    val subscriptionOrder: Any,
    @SerializedName("subtotal")
    val subtotal: String,
    @SerializedName("total")
    val total: String,
    @SerializedName("total_product")
    val totalProduct: Int,
    @SerializedName("total_qty")
    val totalQty: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("updated_by")
    val updatedBy: Int
)