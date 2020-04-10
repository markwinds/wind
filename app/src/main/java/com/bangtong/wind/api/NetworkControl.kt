package com.bangtong.wind.api

import com.bangtong.wind.R
import com.bangtong.wind.data.TinyDBManager
import com.bangtong.wind.model.User
import com.bangtong.wind.model.UserAddress
import com.bangtong.wind.util.LogUtil
import com.bangtong.wind.util.MyApplication
import com.bangtong.wind.util.ToastUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class NetworkControl {

    private val TAG = "NetworkControl_ZBT"
    private val service = WindService.getService()

    fun showNetworkError(){
        ToastUtil.showLong(MyApplication.context.getString(R.string.network_error))
    }

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

    fun insertAddress(
        address:UserAddress,
        onSuccess:(id:Long)->Unit
    ){
        service.insertAddress(address).enqueue(
            object :Callback<Long>{
                override fun onFailure(call: Call<Long>, t: Throwable) {
                    LogUtil.d(TAG,"Insert address failed")
                    showNetworkError()
                }
                override fun onResponse(call: Call<Long>, response: Response<Long>) {
                    if(response.isSuccessful){
                        LogUtil.d(TAG,"Insert address OK")
                        onSuccess(response.body()?:1)
                    }
                }

            }
        )
    }

    fun deleteAddress(
        address:UserAddress,
        onSuccess:(result:Boolean)->Unit
    ){
        service.deleteAddress(address).enqueue(
            object :Callback<Boolean>{
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    LogUtil.d(TAG,"Delete address failed")
                    showNetworkError()
                }
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful){
                        LogUtil.d(TAG,"Delete address OK")
                        onSuccess(response.body()?:false)
                    }
                }

            }
        )
    }

    fun updateAddress(
        address:UserAddress,
        onSuccess:(result:Boolean)->Unit
    ){
        service.updateAddress(address).enqueue(
            object :Callback<Boolean>{
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    LogUtil.d(TAG,"Update address failed")
                    showNetworkError()
                }
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful){
                        LogUtil.d(TAG,"Update address OK")
                        onSuccess(response.body()?:false)
                    }
                }

            }
        )
    }

    fun syncAddress(
        onSuccess:(result:List<UserAddress>)->Unit
    ) {
        service.syncAddress(TinyDBManager.id).enqueue(
            object : Callback<List<UserAddress>> {
                override fun onFailure(call: Call<List<UserAddress>>, t: Throwable) {
                    LogUtil.d(TAG, "Sync address failed")
                    showNetworkError()
                }

                override fun onResponse(
                    call: Call<List<UserAddress>>,
                    response: Response<List<UserAddress>>
                ) {
                    if (response.isSuccessful) {
                        LogUtil.d(TAG, "Sync address OK")
                        onSuccess(response.body() ?: emptyList<UserAddress>())
                    }
                }

            }
        )
    }

}//net