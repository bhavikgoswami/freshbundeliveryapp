package com.witnovus.freshbundeliveryapp.model.uploaddocument.getbankdetail


import com.google.gson.annotations.SerializedName

data class Records(
    @SerializedName("account_name")
    val accountName: String,
    @SerializedName("account_no")
    val accountNo: String,
    @SerializedName("account_type")
    val accountType: String,
    @SerializedName("bank")
    val bank: Bank,
    @SerializedName("bank_id")
    val bankId: Int,
    @SerializedName("branch_code")
    val branchCode: String,
    @SerializedName("branch_name")
    val branchName: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("created_by")
    val createdBy: Any,
    @SerializedName("deleted_by")
    val deletedBy: Any,
    @SerializedName("delivery_user_id")
    val deliveryUserId: Int,
    @SerializedName("file")
    val `file`: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("ifsc_code")
    val ifscCode: String,
    @SerializedName("micr_code")
    val micrCode: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("updated_by")
    val updatedBy: Any
)