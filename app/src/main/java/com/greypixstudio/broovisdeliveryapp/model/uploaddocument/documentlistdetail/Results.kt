package com.greypixstudio.broovisdeliveryapp.model.uploaddocument.documentlistdetail


import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("records")
    val records: Records
)