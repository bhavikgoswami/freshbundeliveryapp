package com.witnovus.freshbundeliveryapp.model.auth.sendotp


import com.google.gson.annotations.SerializedName

data class SendOTPResults(
    @SerializedName("data")
    val `data`: SendOTPData
)