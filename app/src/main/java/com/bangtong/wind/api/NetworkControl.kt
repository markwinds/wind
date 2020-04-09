package com.bangtong.wind.api

import com.bangtong.wind.model.User
import com.bangtong.wind.model.UserAddress
import com.bangtong.wind.util.LogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class NetworkControl {

    private val TAG = "NetworkControl_ZBT"
    private val service = WindService.getService()

    fun signUp(
        id:String,
        pwd:String,
        onSuccess: (result: String) -> Unit,
        onError: (error: String) -> Unit
    ){
        service.signUp(User(id,pwd)).enqueue(
            object :Callback<String>{
                override fun onFailure(call: Call<String>, t: Throwable) {
                    LogUtil.d(TAG,"sign up network failed")
                    onError(t.message ?: "unknown error")
                }
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    LogUtil.d(TAG, "got a response $response")
                    if (response.isSuccessful) {
                        val result = response.body()?: "NO"
                        onSuccess(result)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }

            }
        )
    }

    fun logIn(
        id:String,
        pwd:String,
        onSuccess: (result: String) -> Unit,
        onError: (error: String) -> Unit
    ){
        service.logIn(id,pwd).enqueue(
            object :Callback<String>{
                override fun onFailure(call: Call<String>, t: Throwable) {
                    LogUtil.d(TAG,"log in network failed")
                    onError(t.message ?: "unknown error")
                }
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    LogUtil.d(TAG, "got a response $response")
                    if (response.isSuccessful) {
                        val result = response.body()?: "NO"
                        onSuccess(result)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }

            }
        )
    }

    fun operateAddress(
        address:UserAddress,
        operation:String
    ){
        service.insertAddress(address).enqueue(
            object :Callback<Long>{
                override fun onFailure(call: Call<Long>, t: Throwable) {
                    LogUtil.d(TAG,"Operate address failed")
                }

                override fun onResponse(call: Call<Long>, response: Response<Long>) {
                    LogUtil.d(TAG,"Operate address OK")
                }

            }
        )
    }

}