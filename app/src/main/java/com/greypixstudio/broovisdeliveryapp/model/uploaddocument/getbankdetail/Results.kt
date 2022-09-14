package com.greypixstudio.broovisdeliveryapp.model.uploaddocument.getbankdetail


import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("records")
    val records: Records
)