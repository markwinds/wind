package com.bangtong.wind.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp
import java.util.*

@Entity(tableName = "order_form")
@Parcelize
data class OrderForm (
    @PrimaryKey(autoGenerate = true) @field:SerializedName("id") var id:Long,

    @field:SerializedName("user")val user:String,

    @field:SerializedName("senderName")val senderName:String,
    @field:SerializedName("senderPhone")val senderPhone:String,
    @field:SerializedName("senderProvince")val senderProvince:String,
    @field:SerializedName("senderCity")val senderCity:String,
    @field:SerializedName("senderArea")val senderArea:String,
    @field:SerializedName("senderLocation")val senderLocation:String,

    @field:SerializedName("receiverName")val receiverName:String,
    @field:SerializedName("receiverPhone")val receiverPhone:String,
    @field:SerializedName("receiverProvince")val receiverProvince:String,
    @field:SerializedName("receiverCity")val receiverCity:String,
    @field:SerializedName("receiverArea")val receiverArea:String,
    @field:SerializedName("receiverLocation")val receiverLocation:String,

    @field:SerializedName("remark")val remark:String,
    @field:SerializedName("timeSend")val timeSend: Long,
    @field:SerializedName("timeReceive")val timeReceive: Long,
    @field:SerializedName("boxId")val boxId:Long

): Parcelable{
    constructor(id:Long,user:String,sender:UserAddress,receive:UserAddress,remark: String,timeSend: Long,timeReceive: Long,boxId: Long):this(
        id,
        user,
        sender.name,
        sender.phone,
        sender.province,
        sender.city,
        sender.area,
        sender.location,
        receive.name,
        receive.phone,
        receive.province,
        receive.city,
        receive.area,
        receive.location,
        remark,
        timeSend,
        timeReceive,
        boxId
    )
}