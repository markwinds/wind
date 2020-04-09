package com.bangtong.wind.data

import androidx.lifecycle.LiveData
import com.bangtong.wind.api.WindService
import com.bangtong.wind.db.WindRoomDataBase
import com.bangtong.wind.model.UserAddress
import com.bangtong.wind.util.MyApplication
import com.bangtong.wind.util.TinyDB

// 所有对数据的请求都从这里处理
// 包括网络和本地
class WindRepository {

    private val windRoomDataBase:WindRoomDataBase = WindRoomDataBase.getDatabase()
    private val service = WindService.getService()
    private val addressDao = windRoomDataBase.addressDao()

    fun getAllAddress(): LiveData<List<UserAddress>> {
        return addressDao.getAllAddress(TinyDBManager.id)
    }

    suspend fun insertAddress(address:UserAddress){
        addressDao.insert(address)
    }


}