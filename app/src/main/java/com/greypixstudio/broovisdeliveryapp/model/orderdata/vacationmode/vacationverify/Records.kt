package com.greypixstudio.broovisdeliveryapp.model.orderdata.vacationmode.vacationverify


import com.google.gson.annotations.SerializedName

data class Records(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("current_date")
    val currentDate: String,
    @SerializedName("delivery_user_id")
    val deliveryUserId: Int,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("subscription_order_id")
    val subscriptionOrderId: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_type")
    val userType: String
)