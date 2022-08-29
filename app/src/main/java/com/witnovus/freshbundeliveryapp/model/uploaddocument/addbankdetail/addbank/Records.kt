package com.witnovus.freshbundeliveryapp.model.uploaddocument.addbankdetail.addbank


import com.google.gson.annotations.SerializedName

data class Records(
    @SerializedName("account_name")
    val accountName: String,
    @SerializedName("account_no")
    val accountNo: String,
    @SerializedName("account_type")
    val accountType: String,
    @SerializedName("bank_id")
    val bankId: String,
    @SerializedName("branch_code")
    val branchCode: Any,
    @SerializedName("branch_name")
    val branchName: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("delivery_user_id")
    val deliveryUserId: Int,
    @SerializedName("file")
    val `file`: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("ifsc_code")
    val ifscCode: String,
    @SerializedName("micr_code")
    val micrCode: Any,
    @SerializedName("updated_at")
    val updatedAt: String
)