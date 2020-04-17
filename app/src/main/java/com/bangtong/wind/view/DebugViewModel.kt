package com.bangtong.wind.view

import androidx.lifecycle.*
import com.bangtong.wind.api.NetworkControl
import com.bangtong.wind.data.WindRepository
import com.bangtong.wind.model.BoxIfo
import com.bangtong.wind.util.ToastUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class DebugViewModel(private val state: SavedStateHandle):ViewModel() {

    private val repository = WindRepository()
    private val networkControl = NetworkControl()
    val lockCount:MutableLiveData<Int> = MutableLiveData()
    val unlockCount:MutableLiveData<Int> = MutableLiveData()
    val lockStatus:MutableLiveData<String> = MutableLiveData()
    var lockClickable = true
    var unlockClickable = true

    val boxIfo: MutableLiveData<BoxIfo> by lazy{
        MutableLiveData(BoxIfo(1,1,1,1.toDouble(),1.toDouble(),1))
    }

    fun insertBoxIfoCloud(boxIfo:BoxIfo) = viewModelScope.launch(Dispatchers.IO){
        repository.insertBoxIfoCloud(boxIfo)
    }

    fun lock(boxId:Long, enable: Boolean){
        networkControl.lock(boxId,enable, this::getLockResult)
    }

    private fun getLockResult(boxId:Long,enable:Boolean,result: Boolean){
        viewModelScope.launch {
            if (enable) {
                if (result) {
                    ToastUtil.showLong("Begin use phone bind!")
                    lockCount.postValue(15)
                    while (lockCount.value != 0) {
                        delay(1000)
                        lockCount.postValue(lockCount.value?.minus(1))
                    }
                    lock(boxId, false)
                } else {
                    ToastUtil.showLong("Lock failed, caused by box has been used!")
                    lockClickable = true
                }
            } else {
                if (result) {
                    ToastUtil.showLong("Bind completed!")
                    lockStatus.postValue("Locked")
                } else {
                    ToastUtil.showLong("No order want to bind!")
                }
                lockClickable = true
            }
        }
    }

    fun unlock(boxId:Long, enable: Boolean){
        networkControl.unlock(boxId,enable, this::getUnlockResult)
    }

    private fun getUnlockResult(boxId:Long,enable:Boolean,result: Boolean){
        viewModelScope.launch {
            if (enable) {
                if (result) {
                    ToastUtil.showLong("Wait receive!")
                    unlockCount.postValue(15)
                    while (unlockCount.value != 0) {
                        delay(1000)
                        unlockCount.postValue(unlockCount.value?.minus(1))
                    }
                    unlock(boxId, false)
                } else {
                    ToastUtil.showLong("No order is bind to this box!")
                    unlockClickable = true
                }
            } else {
                if (result) {
                    ToastUtil.showLong("Received!")
                    lockStatus.postValue("Unlocked")
                } else {
                    ToastUtil.showLong("No order want to receive!")
                }
                unlockClickable = true
            }
        }
    }
}