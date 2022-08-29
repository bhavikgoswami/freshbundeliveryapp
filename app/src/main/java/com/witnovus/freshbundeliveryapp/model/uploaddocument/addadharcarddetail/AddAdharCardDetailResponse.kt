package com.witnovus.freshbundeliveryapp.model.uploaddocument.addadharcarddetail


import com.google.gson.annotations.SerializedName

data class AddAdharCardDetailResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("count")
    val count: Count,
    @SerializedName("error")
    val error: List<Any>,
    @SerializedName("message")
    val message: String,
    @SerializedName("results")
    val results: Results,
    @SerializedName("success")
    val success: Boolean
)