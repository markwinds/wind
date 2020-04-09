package com.bangtong.wind.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangtong.wind.R
import com.bangtong.wind.model.UserAddress

class AdsManagerViewHolder(view: View):RecyclerView.ViewHolder(view) {

    private val namePhone: TextView = view.findViewById(R.id.namePhone)
    private val fullAddress: TextView = view.findViewById(R.id.fullAddress)

    init {
        /**
         * 写item的监听事件
         * */
    }

    fun bind(ads:UserAddress?){
        if(ads!=null){
            var temp = ads.name+" "+ads.phone
            namePhone.text = temp
            temp = ads.province+" "+ads.city+" "+ads.area+" "+ads.location
            fullAddress.text = temp
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