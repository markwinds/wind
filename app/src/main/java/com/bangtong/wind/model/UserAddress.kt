package com.bangtong.wind.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "address")
@Parcelize
data class UserAddress(
    @PrimaryKey(autoGenerate = true) @field:SerializedName("id") var id:Long,
    @field:SerializedName("user")val user:String,
    @field:SerializedName("name")val name:String,
    @field:SerializedName("phone")val phone:String,
    @field:SerializedName("province")val province:String,
    @field:SerializedName("city")val city:String,
    @field:SerializedName("area")val area:String,
    @field:SerializedName("location")val location:String,
    @field:SerializedName("live")var live:Boolean
): Parcelable