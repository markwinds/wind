package com.bangtong.wind.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bangtong.wind.model.UserAddress

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg userAddresses:UserAddress)
    @Query("SELECT * FROM address WHERE user=:user AND live=1")
    fun getAllAddress(user:String): LiveData<List<UserAddress>>
    @Delete
    suspend fun delete(vararg userAddresses:UserAddress) // 根据传入结构体的主键自动删除
    @Update
    suspend fun updateAddress(vararg userAddresses:UserAddress)
    @Query("DELETE FROM address")
    suspend fun deleteAll()
    @Query("SELECT * FROM address WHERE id=:id")
    suspend fun getAddressById(id:Long):UserAddress
}