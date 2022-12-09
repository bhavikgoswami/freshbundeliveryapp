package com.greypixstudio.broovisdeliveryapp.model.notification.notificationlist


import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("body")
    val body: String,
    @SerializedName("category_id")
    val categoryId: Any,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("created_by")
    val createdBy: Any,
    @SerializedName("customer_id")
    val customerId: Any,
    @SerializedName("date")
    val date: String,
    @SerializedName("delivery_user_id")
    val deliveryUserId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("order_id")
    val orderId: Any,
    @SerializedName("product_id")
    val productId: Any,
    @SerializedName("status")
    val status: String,
    @SerializedName("subscription_order_id")
    val subscriptionOrderId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("updated_by")
    val updatedBy: Any,
    @SerializedName("read_status")
    val read_status: String,
    val image: String
)