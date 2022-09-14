package com.greypixstudio.broovisdeliveryapp.model.uploaddocument.getbankdetail


import com.google.gson.annotations.SerializedName

data class GetBankDetailResponse(
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