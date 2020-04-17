package com.bangtong.wind.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "box_ifo")
data class BoxIfo (
    @PrimaryKey(autoGenerate = true) @field:SerializedName("id") var id:Long,
    @field:SerializedName("boxId")val boxId:Long,
    @field:SerializedName("time")val time:Long,
    @field:SerializedName("x")val x:Double,
    @field:SerializedName("y")val y:Double,
    @field:SerializedName("temperature")val temperature:Int
)