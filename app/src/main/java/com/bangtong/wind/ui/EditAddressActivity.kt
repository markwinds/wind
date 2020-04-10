package com.bangtong.wind.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bangtong.wind.R
import com.bangtong.wind.data.TinyDBManager
import com.bangtong.wind.model.UserAddress
import com.bangtong.wind.util.GetJsonDataUtil
import com.bangtong.wind.util.LogUtil
import com.bangtong.wind.util.MyActivity
import com.bangtong.wind.view.EditAddressViewModel
import kotlinx.android.synthetic.main.activity_edit_address.*

class EditAddressActivity : MyActivity() {

    private val viewModel by viewModels<EditAddressViewModel>()
    private val TAG = "EditAddressActivity_ZBT"
    private var addressId:Long = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_address)
        //val viewModel by viewModels<EditAddressViewModel>()
        setSupportActionBar(toolbar2)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        area.isFocusable = false
        area.setOnClickListener{
            viewModel.showProvincePicker()
        }
        viewModel.area.observe(this, Observer {
            val temp = it[0]+" "+it[1]+" "+it[2]
            area.setText(temp)
        })
        cancle.setOnClickListener{
            goAdsManagerActivity(false)
        }
        submit.setOnClickListener{
            goAdsManagerActivity(true)
        }
        if (intent != null){
            val tempAddress = intent.getParcelableExtra<UserAddress>("address")
            if(tempAddress != null){
                name.setText(tempAddress.name)
                phone.setText(tempAddress.phone)
                val temp = tempAddress.province+" "+tempAddress.city+" "+ tempAddress.area
                area.setText(temp)
                address.setText(tempAddress.location)
                addressId = tempAddress.id
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return true
    }

    private fun goAdsManagerActivity(result:Boolean){
        if(!result){
            val replyIntent = Intent()
            setResult(Activity.RESULT_CANCELED, replyIntent)
            finish()
        }else{
            if(!TextUtils.isEmpty(name.text) && !TextUtils.isEmpty(phone.text)&&
                !TextUtils.isEmpty(area.text) && !TextUtils.isEmpty(address.text)) {
                val list = area.text.toString().split(" ")
                val tempAddress = UserAddress(addressId, TinyDBManager.id,name.text.toString(),phone.text.toString(),list[0],list[1],
                    list[2],address.text.toString())
                LogUtil.d(TAG,"Commit address ${TinyDBManager.id} ${name.text} ${phone.text} ${list[0]} ${list[1]} ${list[2]} ${address.text}")
                val replyIntent = Intent().putExtra("address",tempAddress)
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }
    }

}
