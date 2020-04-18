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
import com.bangtong.wind.ui.TransmitOrderActivity
import com.bangtong.wind.util.LogUtil
import com.bangtong.wind.util.MyActivity
import com.bangtong.wind.util.MyApplication
import com.bangtong.wind.util.ToastUtil
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.delete_scan.*

class HomeViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    private val holderContext:HomeActivity = MyActivity.getTopActivityWithType("HomeActivity") as HomeActivity
    private val sender: TextView = view.findViewById(R.id.sender)
    private val receiver: TextView = view.findViewById(R.id.receiver)
    private val state: TextView = view.findViewById(R.id.state)
    private val viewModel = holderContext.viewModel
    val swipeRevealLayout: SwipeRevealLayout = view.findViewById(R.id.swipeRevealLayout)
    private val delete:ImageView = view.findViewById(R.id.delete)
    private val scan:ImageView = view.findViewById(R.id.scan)
    private val mainView = view.findViewById<View>(R.id.mainView)
    private val boxId = view.findViewById<TextView>(R.id.boxId)
    private val check = view.findViewById<ImageView>(R.id.check)


    fun bind(order:OrderForm?){
        if(order!=null){
            if (order.receiverPhone == TinyDBManager.id){
                state.text = MyActivity.getTopActivity().getString(R.string.r)
            }else{
                state.text = MyActivity.getTopActivity().getString(R.string.s)
            }
            when(order.boxId){
                (0).toLong() -> {
                    boxId.text = MyApplication.context.getString(R.string.bindBox)
                }
                (-1).toLong() -> {
                    boxId.text = MyApplication.context.getString(R.string.completed)
                }
                else -> boxId.text = order.boxId.toString()
            }
            sender.text = viewModel.getTextByOrderSender(order)
            receiver.text = viewModel.getTextByOrderReceiver(order)
            delete.setOnClickListener{
                MaterialDialog(holderContext).show {
                    message(R.string.confirm_delete)
                    positiveButton(R.string.confirm){
                        holderContext.viewModel.deleteCloud(order)
                    }
                    negativeButton(android.R.string.cancel)
                    cornerRadius(10f) // 角半径
                }
            }
            scan.setOnClickListener{
                holderContext.viewModel.orderId = order.id
                IntentIntegrator(holderContext).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)// 扫码的类型,可选：一维码，二维码，一/二维码
                    //.setPrompt("请对准二维码")// 设置提示语
                    .setCameraId(0)// 选择摄像头,可使用前置或者后置
                    .setBeepEnabled(true)// 是否开启声音,扫完码之后会"哔"的一声
                    .setBarcodeImageEnabled(false)// 扫完码之后生成二维码的图片
                    .initiateScan();// 初始化扫码
            }
            check.setOnClickListener{
                holderContext.viewModel.unbindOrderBox(order.boxId)
            }
            when(order.boxId){
                0.toLong() -> {
                    //swipeRevealLayout.setLockDrag(false)
                    scan.visibility = View.VISIBLE
                    delete.visibility = View.VISIBLE
                    check.visibility = View.GONE
                }
                (-1).toLong() -> {
                    //swipeRevealLayout.setLockDrag(false)
                    scan.visibility = View.GONE
                    delete.visibility = View.VISIBLE
                    check.visibility = View.GONE
                }
                else -> {
                    //swipeRevealLayout.setLockDrag(true)
                    scan.visibility = View.GONE
                    delete.visibility = View.GONE
                    if(order.receiverPhone == TinyDBManager.id){
                        check.visibility = View.VISIBLE
                    }else{
                        check.visibility = View.GONE
                    }
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
                        holderContext.goCompletedOrderActivity(order)
                    }
                    else -> {
                        val intent = Intent(holderContext,TransmitOrderActivity::class.java)
                        intent.putExtra("order",order)
                        holderContext.startActivity(intent)
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