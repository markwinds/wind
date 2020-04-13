package com.bangtong.wind.api

import androidx.lifecycle.MutableLiveData
import com.bangtong.wind.R
import com.bangtong.wind.data.TinyDBManager
import com.bangtong.wind.model.OrderForm
import com.bangtong.wind.model.User
import com.bangtong.wind.model.UserAddress
import com.bangtong.wind.util.LogUtil
import com.bangtong.wind.util.MyApplication
import com.bangtong.wind.util.ToastUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class NetworkControl {

    companion object{
        val callBackComplete: MutableLiveData<String> = MutableLiveData()
    }

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
        callBackComplete.postValue("Show progress bar")
        service.insertAddress(address).enqueue(
            object :Callback<Long>{
                override fun onFailure(call: Call<Long>, t: Throwable) {
                    LogUtil.d(TAG,"Insert address failed")
                    showNetworkError()
                    callBackComplete.postValue("Hide progress bar")
                }
                override fun onResponse(call: Call<Long>, response: Response<Long>) {
                    if(response.isSuccessful){
                        LogUtil.d(TAG,"Insert address OK")
                        onSuccess(response.body()?:1)
                    }
                    callBackComplete.postValue("Hide progress bar")
                }

            }
        )
    }

    fun deleteAddress(
        address:UserAddress,
        onSuccess:(result:Boolean)->Unit
    ){
        callBackComplete.postValue("Show progress bar")
        service.deleteAddress(address).enqueue(
            object :Callback<Boolean>{
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    LogUtil.d(TAG,"Delete address failed")
                    showNetworkError()
                    callBackComplete.postValue("Hide progress bar")
                }
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful){
                        LogUtil.d(TAG,"Delete address OK")
                        onSuccess(response.body()?:false)
                    }
                    callBackComplete.postValue("Hide progress bar")
                }

            }
        )
    }

    fun updateAddress(
        address:UserAddress,
        onSuccess:(result:Boolean)->Unit
    ){
        callBackComplete.postValue("Show progress bar")
        service.updateAddress(address).enqueue(
            object :Callback<Boolean>{
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    LogUtil.d(TAG,"Update address failed")
                    showNetworkError()
                    callBackComplete.postValue("Hide progress bar")
                }
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful){
                        LogUtil.d(TAG,"Update address OK")
                        onSuccess(response.body()?:false)
                    }
                    callBackComplete.postValue("Hide progress bar")
                }

            }
        )
    }

    fun syncAddress(
        onError:()->Unit,
        onSuccess:(result:List<UserAddress>)->Unit
    ) {
        service.syncAddress(TinyDBManager.id).enqueue(
            object : Callback<List<UserAddress>> {
                override fun onFailure(call: Call<List<UserAddress>>, t: Throwable) {
                    LogUtil.d(TAG, "Sync address failed")
                    showNetworkError()
                    onError()
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

    fun insertOrder(
        order:OrderForm,
        onSuccess:(id:Long)->Unit
    ){
        callBackComplete.postValue("Show progress bar")
        service.insertOrder(order).enqueue(
            object :Callback<Long>{
                override fun onFailure(call: Call<Long>, t: Throwable) {
                    LogUtil.d(TAG,"Insert order failed")
                    showNetworkError()
                    callBackComplete.postValue("Hide progress bar")
                }
                override fun onResponse(call: Call<Long>, response: Response<Long>) {
                    if(response.isSuccessful){
                        LogUtil.d(TAG,"Insert order OK")
                        onSuccess(response.body()?:1)
                    }
                    callBackComplete.postValue("Hide progress bar")
                }

            }
        )
    }

    fun syncOrder(
        onSuccess:(result:List<OrderForm>)->Unit
    ) {
        callBackComplete.postValue("Show progress bar")
        service.syncOrder(TinyDBManager.id).enqueue(
            object : Callback<List<OrderForm>> {
                override fun onFailure(call: Call<List<OrderForm>>, t: Throwable) {
                    LogUtil.d(TAG, "Sync order failed")
                    showNetworkError()
                    callBackComplete.postValue("Hide progress bar")
                }
                override fun onResponse(
                    call: Call<List<OrderForm>>,
                    response: Response<List<OrderForm>>
                ) {
                    if (response.isSuccessful) {
                        LogUtil.d(TAG, "Sync order OK")
                        onSuccess(response.body() ?: emptyList<OrderForm>())
                    }
                    callBackComplete.postValue("Hide progress bar")
                }

            }
        )
    }

    fun deleteOrder(
        order: OrderForm,
        onSuccess:(result:Boolean)->Unit
    ){
        callBackComplete.postValue("Show progress bar")
        service.deleteOrder(order).enqueue(
            object :Callback<Boolean>{
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    LogUtil.d(TAG,"Delete order failed")
                    showNetworkError()
                    callBackComplete.postValue("Hide progress bar")
                }
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful){
                        LogUtil.d(TAG,"Delete address OK")
                        onSuccess(response.body()?:false)
                    }
                    callBackComplete.postValue("Hide progress bar")
                }

            }
        )
    }

}//net