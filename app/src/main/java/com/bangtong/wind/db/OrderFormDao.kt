package com.bangtong.wind.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bangtong.wind.model.OrderForm

@Dao
interface OrderFormDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg order: OrderForm)
    @Query("SELECT * FROM order_form WHERE user=:user")
    fun getAllOrder(user:String): LiveData<List<OrderForm>>
    @Delete
    suspend fun delete(vararg orderForm:OrderForm) // 根据传入结构体的主键自动删除
    @Update
    suspend fun updateAddress(vararg orderForm:OrderForm)
    @Query("DELETE FROM order_form")
    suspend fun deleteAll()
}