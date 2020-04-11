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
    @field:SerializedName("senderId")val senderId:Long,
    @field:SerializedName("receiverId")val receiverId:Long,
    @field:SerializedName("remark")val remark:String,
    @field:SerializedName("time")val time: Long,
    @field:SerializedName("boxId")val boxId:Long,
    @field:SerializedName("user")val user:String
): Parcelable