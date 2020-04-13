package com.bangtong.wind.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bangtong.wind.R
import com.bangtong.wind.data.TinyDBManager
import com.bangtong.wind.model.OrderForm
import com.bangtong.wind.model.UserAddress
import com.bangtong.wind.util.MyActivity
import com.bangtong.wind.util.TimeControl
import com.bangtong.wind.view.AddOrderViewModel
import kotlinx.android.synthetic.main.activity_add_order.*

class AddOrderActivity : MyActivity() {

    private val senderCode = 1
    private val receiverCode = 2
    private val viewModel by viewModels<AddOrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        sender.setOnClickListener{
            val intent = Intent(this@AddOrderActivity, AdsManagerActivity::class.java)
            intent.putExtra("AddOrderActivity",senderCode)
            startActivityForResult(intent, senderCode)
        }
        receiver.setOnClickListener{
            val intent = Intent(this@AddOrderActivity, AdsManagerActivity::class.java)
            intent.putExtra("AddOrderActivity",receiverCode)
            startActivityForResult(intent, receiverCode)
        }
        submit.setOnClickListener{
            if(viewModel.receiverText.value != null && viewModel.senderText.value != null){
                val order = OrderForm(0,TinyDBManager.id,viewModel.senderText.value!!,viewModel.receiverText.value!!,
                    remark.text.toString(),TimeControl.getTimeToLong(),0,0)
                val replyIntent = Intent().putExtra("order",order)
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }
        viewModel.receiverText.observe(this, Observer {
            receiver.text = viewModel.getTextByAddress(viewModel.receiverText.value!!)
        })
        viewModel.senderText.observe(this, Observer {
            sender.text = viewModel.getTextByAddress(viewModel.senderText.value!!)
        })
        if(viewModel.receiverText.value != null){
            receiver.text = viewModel.getTextByAddress(viewModel.receiverText.value!!)
        }
        if(viewModel.senderText.value != null){
            sender.text = viewModel.getTextByAddress(viewModel.senderText.value!!)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode){
                senderCode -> {
                    data?.getParcelableExtra<UserAddress>("address")?.let {
                        viewModel.senderText.postValue(it)
                    }
                }
                receiverCode -> {
                    data?.getParcelableExtra<UserAddress>("address")?.let {
                        viewModel.receiverText.postValue(it)
                    }
                }
            }

        }
    }



}
