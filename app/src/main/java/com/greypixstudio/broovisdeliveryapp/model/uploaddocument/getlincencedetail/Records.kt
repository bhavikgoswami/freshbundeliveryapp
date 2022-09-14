package com.greypixstudio.broovisdeliveryapp.model.uploaddocument.getlincencedetail


import com.google.gson.annotations.SerializedName

data class Records(
    @SerializedName("back_side")
    val backSide: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("created_by")
    val createdBy: Any,
    @SerializedName("deleted_by")
    val deletedBy: Any,
    @SerializedName("delivery_user_id")
    val deliveryUserId: Int,
    @SerializedName("document_type")
    val documentType: String,
    @SerializedName("front_side")
    val frontSide: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("licence_expiry_date")
    val licenceExpiryDate: String,
    @SerializedName("licence_register_date")
    val licenceRegisterDate: String,
    @SerializedName("licence_type")
    val licenceType: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("updated_by")
    val updatedBy: Any
)