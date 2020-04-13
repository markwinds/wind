package com.bangtong.wind.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.MenuItem
import com.bangtong.wind.R
import com.bangtong.wind.model.OrderForm
import com.bangtong.wind.util.MyActivity
import com.bangtong.wind.util.TimeControl
import kotlinx.android.synthetic.main.activity_completed_order.*

class CompletedOrderActivity : MyActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_order)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        val order = intent.getParcelableExtra<OrderForm>("order")
        sender.text = getTextByOrderSender(order!!)
        receiver.text = getTextByOrderReceiver(order)
        time.text = getTextByOrderTime(order)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }

    private fun getTextByOrderSender(order:OrderForm): SpannableString {
        val top = order.senderName+" "+order.senderPhone
        val bottom = order.senderProvince+order.senderCity+order.senderArea+order.senderLocation
        val spannableString = SpannableString(top+"\n"+bottom)
        spannableString.setSpan(AbsoluteSizeSpan(75),0,top.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(AbsoluteSizeSpan(38),top.length+1,top.length+bottom.length+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    private fun getTextByOrderReceiver(order:OrderForm): SpannableString {
        val top = order.receiverName+" "+order.receiverPhone
        val bottom = order.receiverProvince+order.receiverCity+order.receiverArea+order.receiverLocation
        val spannableString = SpannableString(top+"\n"+bottom)
        spannableString.setSpan(AbsoluteSizeSpan(75),0,top.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(AbsoluteSizeSpan(38),top.length+1,top.length+bottom.length+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    private fun getTextByOrderTime(order:OrderForm): SpannableString{
        val sendTime = TimeControl.convertLongToTime(order.timeSend) + " " + getString(R.string.s)
        val receiveTime = TimeControl.convertLongToTime(order.timeReceive)+ " " + getString(R.string.r)
        val spannableString = SpannableString(sendTime+"\n"+receiveTime)
        spannableString.setSpan(AbsoluteSizeSpan(40),0,sendTime.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(AbsoluteSizeSpan(40),sendTime.length+1,sendTime.length+receiveTime.length+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannableString
    }

}
