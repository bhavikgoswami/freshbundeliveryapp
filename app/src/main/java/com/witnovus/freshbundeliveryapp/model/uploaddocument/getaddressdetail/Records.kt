package com.witnovus.freshbundeliveryapp.model.uploaddocument.getaddressdetail


import com.google.gson.annotations.SerializedName

data class Records(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("created_by")
    val createdBy: Any,
    @SerializedName("current_address")
    val currentAddress: String,
    @SerializedName("current_area")
    val currentArea: String,
    @SerializedName("current_building_name")
    val currentBuildingName: String,
    @SerializedName("current_city")
    val currentCity: String,
    @SerializedName("current_landmark")
    val currentLandmark: String,
    @SerializedName("current_latitudes")
    val currentLatitudes: String,
    @SerializedName("current_longitudes")
    val currentLongitudes: String,
    @SerializedName("current_state")
    val currentState: String,
    @SerializedName("current_zipcode")
    val currentZipcode: Int,
    @SerializedName("deleted_by")
    val deletedBy: Any,
    @SerializedName("delivery_user_id")
    val deliveryUserId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("permanent_address")
    val permanentAddress: String,
    @SerializedName("permanent_area")
    val permanentArea: String,
    @SerializedName("permanent_building_name")
    val permanentBuildingName: String,
    @SerializedName("permanent_city")
    val permanentCity: String,
    @SerializedName("permanent_landmark")
    val permanentLandmark: String,
    @SerializedName("permanent_latitudes")
    val permanentLatitudes: String,
    @SerializedName("permanent_longitudes")
    val permanentLongitudes: String,
    @SerializedName("permanent_state")
    val permanentState: String,
    @SerializedName("permanent_zipcode")
    val permanentZipcode: Int,
    @SerializedName("same_as")
    val sameAs: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("updated_by")
    val updatedBy: Any
)