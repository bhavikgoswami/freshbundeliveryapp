package com.witnovus.freshbundeliveryapp.model.auth.getuserdetail


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("address")
    val address: Any,
    @SerializedName("alternative_contact_no")
    val alternativeContactNo: String,
    @SerializedName("birthdate")
    val birthdate: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("created_by")
    val createdBy: Int,
    @SerializedName("deleted_by")
    val deletedBy: Any,
    @SerializedName("device_type")
    val deviceType: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firebase_token")
    val firebaseToken: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("has_license")
    val hasLicense: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: Any,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("no_of_family_member")
    val noOfFamilyMember: Int,
    @SerializedName("paid_by")
    val paidBy: Any,
    @SerializedName("primary_contact_no")
    val primaryContactNo: String,
    @SerializedName("salary")
    val salary: Any,
    @SerializedName("status")
    val status: String,
    @SerializedName("store_id")
    val storeId: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("updated_by")
    val updatedBy: Int,
    @SerializedName("vehicle_id")
    val vehicleId: Int,
    @SerializedName("verified_status")
    val verifiedStatus: String
)