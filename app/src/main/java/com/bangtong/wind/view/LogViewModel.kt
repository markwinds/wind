package com.bangtong.wind.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangtong.wind.R
import com.bangtong.wind.api.NetworkControl
import com.bangtong.wind.data.TinyDBManager
import com.bangtong.wind.util.LogUtil
import com.bangtong.wind.util.MyApplication
import com.bangtong.wind.util.TinyDB
import com.bangtong.wind.util.ToastUtil

private const val TAG = "LogViewModel_ZBT"

class LogViewModel:ViewModel(){

    private val networkErrors = MutableLiveData<String>()
    private val networkControl = NetworkControl()
    val logState = MutableLiveData<String>()


    fun logIn(id:String, pwd:String){
        networkControl.logIn(id,pwd,{result ->
            if (result == "OK"){
                LogUtil.d(TAG,"log in success")
                TinyDBManager.saveData(id,pwd)
                TinyDBManager.id = id
                TinyDBManager.pwd = pwd
                logState.postValue("OK")
            }else{
                LogUtil.d(TAG,"log in failed")
                ToastUtil.showLong(MyApplication.context.getString(R.string.log_in_failed))
            }
        },{error ->
            networkErrors.postValue(error) // 记录网络错误的原因
            ToastUtil.showLong(MyApplication.context.getString(R.string.check_network))
        })
    }

    fun signUp(id:String,pwd: String){
        networkControl.signUp(id,pwd,{result ->
            if (result == "OK"){
                LogUtil.d(TAG,"sign up success")
                ToastUtil.showLong(MyApplication.context.getString(R.string.sign_up_successful))
            }else{
                LogUtil.d(TAG,"sign up failed")
                ToastUtil.showLong(MyApplication.context.getString(R.string.sign_up_failed))
            }
        },{error ->
            networkErrors.postValue(error) // 记录网络错误的原因
            ToastUtil.showLong(MyApplication.context.getString(R.string.check_network))
        })
    }

}