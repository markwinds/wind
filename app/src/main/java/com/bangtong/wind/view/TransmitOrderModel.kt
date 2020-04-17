package com.bangtong.wind.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangtong.wind.data.WindRepository
import com.bangtong.wind.model.BoxIfo
import com.bangtong.wind.model.OrderForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransmitOrderModel: ViewModel() {

    private val repository = WindRepository()
    lateinit var boxIfo: LiveData<List<BoxIfo>>
    lateinit var order:OrderForm

    fun getBoxIfoCloud() = viewModelScope.launch(Dispatchers.IO){
        repository.getBoxIfoCloud(order.boxId)
    }

    fun initData(order:OrderForm){
        this.order = order
        boxIfo = repository.getBoxIfo(order.boxId)
    }
}