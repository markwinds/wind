package com.bangtong.wind.view

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.FtsOptions
import com.bangtong.wind.data.WindRepository
import com.bangtong.wind.model.OrderForm
import com.bangtong.wind.model.UserAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {

    private val repository = WindRepository()
    val orderForms = repository.getAllOrder()
    var debugCount = 0
    var orderId:Long = 0

    fun insertCloud(order: OrderForm) = viewModelScope.launch(Dispatchers.IO){
        repository.insertOrderCloud(order)
    }

    fun syncOrder() = viewModelScope.launch(Dispatchers.IO) {
        repository.syncOrderCloud()
    }

    fun deleteCloud(order: OrderForm) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteOrderCloud(order)
    }

    fun getTextByOrderSender(order: OrderForm): SpannableString {
        val top = order.senderName
        val bottom = order.senderProvince+order.senderCity
        val spannableString = SpannableString(top+"\n"+bottom)
        spannableString.setSpan(AbsoluteSizeSpan(60),0,top.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(AbsoluteSizeSpan(30),top.length+1,top.length+bottom.length+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    fun getTextByOrderReceiver(order: OrderForm): SpannableString {
        val top = order.receiverName
        val bottom = order.receiverProvince+order.receiverCity
        val spannableString = SpannableString(top+"\n"+bottom)
        spannableString.setSpan(AbsoluteSizeSpan(60),0,top.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(AbsoluteSizeSpan(30),top.length+1,top.length+bottom.length+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    fun bindOrderBox(boxId:Long,orderId:Long){
        repository.bindOrderBox(boxId, orderId)
    }

    fun unbindOrderBox(boxId: Long){
        repository.unbindOrderBox(boxId)
    }
}