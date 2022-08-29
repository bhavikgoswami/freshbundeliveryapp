package com.witnovus.freshbundeliveryapp.model.uploaddocument.addbankdetail.banklist


import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("records")
    val records: List<Record>
)