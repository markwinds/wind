package com.bangtong.wind.data

import androidx.lifecycle.LiveData
import com.bangtong.wind.api.NetworkControl
import com.bangtong.wind.api.WindService
import com.bangtong.wind.db.OrderFormDao
import com.bangtong.wind.db.WindRoomDataBase
import com.bangtong.wind.model.OrderForm
import com.bangtong.wind.model.UserAddress
import com.bangtong.wind.util.MyApplication
import com.bangtong.wind.util.TinyDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// 所有对数据的请求都从这里处理
// 包括网络和本地
class WindRepository {

    private val windRoomDataBase:WindRoomDataBase = WindRoomDataBase.getDatabase()
    private val service = WindService.getService()
    private val networkControl = NetworkControl()
    private val addressDao = windRoomDataBase.addressDao()
    private val orderFormDao = windRoomDataBase.orderFormDao()

    fun getAllAddress(): LiveData<List<UserAddress>> {
        return addressDao.getAllAddress(TinyDBManager.id)
    }

    suspend fun insertAddress(address:UserAddress){
        addressDao.insert(address)
    }

    suspend fun insertAddressCloud(address:UserAddress){
        networkControl.insertAddress(address) {
            address.id = it
            if(address.id != 1.toLong()){
                GlobalScope.launch {
                    addressDao.insert(address)
                }
            }
        }
    }

    suspend fun deleteAddressCloud(address:UserAddress){
        networkControl.deleteAddress(address) {
            if (it){
                GlobalScope.launch {
                    addressDao.delete(address)
                }
            }
        }
    }

    suspend fun updateAddressCloud(address:UserAddress){
        networkControl.updateAddress(address) {
            if (it){
                GlobalScope.launch {
                    addressDao.updateAddress(address)
                }
            }
        }
    }

    suspend fun syncAddressCloud(
        onComplete:()->Unit
    ){
        networkControl.syncAddress({
            onComplete()
        }, {
            if (it.isNotEmpty()){
                GlobalScope.launch {
                    addressDao.deleteAll()
                    for (ads in it){
                        addressDao.insert(ads)
                    }
                    onComplete()
                }
            }
        })
    }

    fun getAllOrder():LiveData<List<OrderForm>> =
        orderFormDao.getAllOrder(TinyDBManager.id)


}