package com.bangtong.wind.util

import android.widget.Toast

class ToastUtil {

    companion object{
        fun showShort(msg:String){
            Toast.makeText(MyApplication.context, msg, Toast.LENGTH_SHORT).show()
        }
        fun showLong(msg:String){
            Toast.makeText(MyApplication.context, msg, Toast.LENGTH_LONG).show()
        }
    }
}