package com.bangtong.wind.model

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id") val id:String,
    @SerializedName("password") val password:String
)