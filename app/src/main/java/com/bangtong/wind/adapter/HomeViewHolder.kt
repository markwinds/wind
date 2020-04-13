package com.bangtong.wind.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.bangtong.wind.R
import com.bangtong.wind.data.TinyDBManager
import com.bangtong.wind.model.OrderForm
import com.bangtong.wind.ui.HomeActivity
import com.bangtong.wind.util.LogUtil
import com.bangtong.wind.util.MyActivity
import com.bangtong.wind.util.ToastUtil
import com.chauthai.swipereveallayout.SwipeRevealLayout
import kotlinx.android.synthetic.main.delete_scan.*

class HomeViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    private val sender: TextView = view.findViewById(R.id.sender)
    private val receiver: TextView = view.findViewById(R.id.receiver)
    private val state: TextView = view.findViewById(R.id.state)
    private val viewModel = (MyActivity.getTopActivity() as HomeActivity).viewModel
    val swipeRevealLayout: SwipeRevealLayout = view.findViewById(R.id.swipeRevealLayout)
    private val delete:ImageView = view.findViewById(R.id.delete)
    private val scan:ImageView = view.findViewById(R.id.scan)
    private val mainView = view.findViewById<View>(R.id.mainView)


    fun bind(order:OrderForm?){
        if(order!=null){
            if (order.receiverPhone == TinyDBManager.id){
                state.text = MyActivity.getTopActivity().getString(R.string.r)
            }else{
                state.text = MyActivity.getTopActivity().getString(R.string.s)
            }
            sender.text = viewModel.getTextByOrderSender(order)
            receiver.text = viewModel.getTextByOrderReceiver(order)
            delete.setOnClickListener{
                MaterialDialog((MyActivity.getTopActivity() as HomeActivity)).show {
                    message(R.string.confirm_delete)
                    positiveButton(R.string.confirm){
                        (MyActivity.getTopActivity() as HomeActivity).viewModel.deleteCloud(order)
                    }
                    negativeButton(android.R.string.cancel)
                    cornerRadius(10f) // 角半径
                }
            }
            scan.setOnClickListener{
                (MyActivity.getTopActivity() as HomeActivity).goScanActivity()
            }
            when(order.boxId){
                0.toLong() -> {
                    swipeRevealLayout.setLockDrag(false)
                    scan.visibility = View.VISIBLE
                }
                (-1).toLong() -> {
                    swipeRevealLayout.setLockDrag(false)
                    scan.visibility = View.GONE
                }
                else -> {
                    swipeRevealLayout.setLockDrag(true)
                }
            }
            mainView.setOnClickListener{
                when(order.boxId){
                    0.toLong() -> {
                        if (swipeRevealLayout.isClosed){
                            swipeRevealLayout.open(true)
                        }else{
                            swipeRevealLayout.close(true)
                        }
                    }
                    (-1).toLong() -> {
                        (MyActivity.getTopActivity() as HomeActivity).goCompletedOrderActivity(order)
                    }
                    else -> {
                        //LogUtil.d("hello","tra")
                    }
                }
            }
        }
    }

    companion object{
        fun create(parent: ViewGroup): HomeViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.order_view_item, parent, false)
            return HomeViewHolder(view)
        }
    }
}