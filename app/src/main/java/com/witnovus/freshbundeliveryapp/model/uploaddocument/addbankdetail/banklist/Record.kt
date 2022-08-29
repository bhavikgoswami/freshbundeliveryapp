package com.witnovus.freshbundeliveryapp.model.uploaddocument.addbankdetail.banklist


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Record(
    @SerializedName("bank_logo")
    val bankLogo: String?,
    @SerializedName("bank_name")
    val bankName: String?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("created_by")
    val createdBy: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("updated_by")
    val updatedBy: Any,
    var isSelected: Boolean = false
    )