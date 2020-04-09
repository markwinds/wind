package com.bangtong.wind.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangtong.wind.api.NetworkControl
import com.bangtong.wind.data.WindRepository
import com.bangtong.wind.model.UserAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdsManagerViewModel:ViewModel() {

    private val repository = WindRepository()
    val addresses:LiveData<List<UserAddress>> = repository.getAllAddress() // 绑定数据，之后数据库的所有变化都会实时更新到这里

    fun insert(address:UserAddress) = viewModelScope.launch(Dispatchers.IO){
        repository.insertAddress(address)
    }

    fun test(address:UserAddress) = viewModelScope.launch(Dispatchers.IO){
        NetworkControl().operateAddress(address,"ddd")
    }
}