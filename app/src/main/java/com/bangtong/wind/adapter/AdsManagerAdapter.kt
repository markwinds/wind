package com.bangtong.wind.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bangtong.wind.model.UserAddress

class AdsManagerAdapter(private val showButton:Boolean):ListAdapter<UserAddress, ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<UserAddress>() {
            override fun areItemsTheSame(oldItem: UserAddress, newItem: UserAddress): Boolean =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: UserAddress, newItem: UserAddress): Boolean =
                oldItem == newItem // 如果类没有声明为data这里会报错
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return AdsManagerViewHolder.create(parent,showButton)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as AdsManagerViewHolder).bind(getItem(position))
    }
}