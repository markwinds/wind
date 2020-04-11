package com.bangtong.wind.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.bangtong.wind.R
import com.bangtong.wind.model.UserAddress
import com.bangtong.wind.ui.AdsManagerActivity
import com.bangtong.wind.util.MyActivity
import com.bangtong.wind.util.MyApplication

class AdsManagerViewHolder(val view: View):RecyclerView.ViewHolder(view) {

    private val namePhone: TextView = view.findViewById(R.id.namePhone)
    private val fullAddress: TextView = view.findViewById(R.id.fullAddress)
    private val delete:Button = view.findViewById(R.id.delete)
    private val edit:Button = view.findViewById(R.id.edit)

    fun bind(ads:UserAddress?){
        if(ads!=null){
            var temp = ads.name+" "+ads.phone
            namePhone.text = temp
            temp = ads.province+" "+ads.city+" "+ads.area+" "+ads.location
            fullAddress.text = temp
            delete.setOnClickListener{
                MaterialDialog((MyActivity.getTopActivity() as AdsManagerActivity)).show {
                    message(R.string.confirm_delete)
                    positiveButton(R.string.confirm){
                        (MyActivity.getTopActivity() as AdsManagerActivity).viewModel.deleteCloud(ads)
                    }
                    negativeButton(android.R.string.cancel)
                    cornerRadius(10f) // 角半径
                }
            }
            edit.setOnClickListener{
                (MyActivity.getTopActivity() as AdsManagerActivity).goEditAddressActivity(ads)
            }
            view.setOnClickListener{
                (MyActivity.getTopActivity() as AdsManagerActivity).goAddOrderActivity(ads)
            }
        }
    }

    companion object{
        fun create(parent: ViewGroup,showButton:Boolean): AdsManagerViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_address_view_item, parent, false)
            if (!showButton){
                view.findViewById<View>(R.id.divider).visibility = View.GONE
                view.findViewById<Button>(R.id.edit).visibility = View.GONE
                view.findViewById<Button>(R.id.delete).visibility = View.GONE
            }
            return AdsManagerViewHolder(view)
        }
    }
}