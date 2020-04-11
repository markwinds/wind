package com.bangtong.wind.view

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bangtong.wind.model.UserAddress


class AddOrderViewModel: ViewModel() {

    val senderText: MutableLiveData<UserAddress> = MutableLiveData()
    val receiverText: MutableLiveData<UserAddress> = MutableLiveData()
    //val remark: MutableLiveData<String> = MutableLiveData()

    fun getTextByAddress(address:UserAddress):SpannableString{
        val top = address.name+" "+address.phone
        val bottom = address.province+address.city+address.area+address.location
        val spannableString = SpannableString(top+"\n"+bottom)
        spannableString.setSpan(AbsoluteSizeSpan(75),0,top.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(AbsoluteSizeSpan(38),top.length+1,top.length+bottom.length+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannableString
    }
}