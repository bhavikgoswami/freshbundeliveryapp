package com.greypixstudio.broovisdeliveryapp.model.auth.getuserdetail


import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("data")
    val  data : Data
)