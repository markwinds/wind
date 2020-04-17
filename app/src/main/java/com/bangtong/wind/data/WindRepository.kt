package com.bangtong.wind.data

import androidx.lifecycle.LiveData
import com.bangtong.wind.R
import com.bangtong.wind.api.NetworkControl
import com.bangtong.wind.api.WindService
import com.bangtong.wind.db.OrderFormDao
import com.bangtong.wind.db.WindRoomDataBase
import com.bangtong.wind.model.BoxIfo
import com.bangtong.wind.model.OrderForm
import com.bangtong.wind.model.UserAddress
import com.bangtong.wind.util.MyApplication
import com.bangtong.wind.util.TinyDB
import com.bangtong.wind.util.ToastUtil
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
    private val boxIfoDao = windRoomDataBase.boxIfoDao()

    fun getAllAddress(): LiveData<List<UserAddress>> {
        return addressDao.getAllAddress(TinyDBManager.id)
    }

    suspend fun getAddressById(id:Long):UserAddress =
        addressDao.getAddressById(id)

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

    suspend fun insertOrderCloud(order:OrderForm){
        networkControl.insertOrder(order) {
            order.id = it
            if(order.id != 1.toLong()){
                GlobalScope.launch {
                    orderFormDao.insert(order)
                }
            }
        }
    }

    suspend fun syncOrderCloud(
    ){
        networkControl.syncOrder() {
            if (it.isNotEmpty()){
                GlobalScope.launch {
                    orderFormDao.deleteAll()
                    for (order in it){
                        orderFormDao.insert(order)
                    }
                }
            }
        }
    }

    suspend fun deleteOrderCloud(order: OrderForm){
        networkControl.deleteOrder(order) {
            if (it){
                GlobalScope.launch {
                    orderFormDao.delete(order)
                }
            }
        }
    }

    fun getBoxIfo(boxId:Long):LiveData<List<BoxIfo>> =
        boxIfoDao.getBoxIfo(boxId)

    suspend fun getBoxIfoCloud(boxId:Long){
        networkControl.getBoxIfo(boxId) {
            if (it.isNotEmpty()){
                GlobalScope.launch {
                    boxIfoDao.insert(it)
                }
            }
        }
    }

    suspend fun insertBoxIfoCloud(boxIfo: BoxIfo){
        networkControl.insertBoxIfo(boxIfo){
            boxIfo.id = it
            if (it != 0.toLong()){
                GlobalScope.launch {
                    withContext(Dispatchers.Main){
                        ToastUtil.showLong("Insert OK")
                    }
                    boxIfoDao.insert(arrayListOf(boxIfo))
                }
            }
        }
    }

    fun bindOrderBox(boxId: Long,orderId:Long){
        networkControl.bindOrderBox(boxId,orderId){
            GlobalScope.launch {
                withContext(Dispatchers.Main){
                    if(it){
                        ToastUtil.showLong(MyApplication.context.getString(R.string.bind_ok))
                    }else{
                        ToastUtil.showLong(MyApplication.context.getString(R.string.bind_failed))
                    }
                }
            }
        }
    }

    fun unbindOrderBox(boxId: Long){
        networkControl.unbindOrderBox(boxId){
            GlobalScope.launch {
                withContext(Dispatchers.Main){
                    if(it){
                        ToastUtil.showLong(MyApplication.context.getString(R.string.unbind_ok))
                    }else{
                        ToastUtil.showLong(MyApplication.context.getString(R.string.unbind_failed))
                    }
                }
            }
        }
    }

//    fun lock(boxId:Long,enable:Boolean){
//        networkControl.lock(boxId,enable)
//    }
//
//    fun unlock(boxId:Long,enable:Boolean){
//        networkControl.unlock(boxId,enable)
//    }

}