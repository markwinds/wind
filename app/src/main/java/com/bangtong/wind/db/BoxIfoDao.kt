package com.bangtong.wind.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bangtong.wind.model.BoxIfo

@Dao
interface BoxIfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(boxes:List<BoxIfo>)
    @Query("SELECT * FROM box_ifo WHERE boxId=:boxId ORDER BY time ASC")
    fun getBoxIfo(boxId:Long): LiveData<List<BoxIfo>>
    @Delete
    suspend fun delete(vararg boxes:BoxIfo) // 根据传入结构体的主键自动删除
    @Query("DELETE FROM box_ifo")
    suspend fun deleteAll()
}