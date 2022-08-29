package com.witnovus.freshbundeliveryapp.model.uploaddocument.getbankdetail


import com.google.gson.annotations.SerializedName

data class Bank(
    @SerializedName("bank_logo")
    var bankLogo: String,
    @SerializedName("bank_name")
    var bankName: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("created_by")
    val createdBy: Int,
    @SerializedName("id")
    var id: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("updated_by")
    val updatedBy: Any
)