package com.bangtong.wind.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangtong.wind.model.OrderForm
import com.chauthai.swipereveallayout.ViewBinderHelper

class HomeAdapter : ListAdapter<OrderForm, RecyclerView.ViewHolder>(COMPARATOR){

    private val viewBinderHelper = ViewBinderHelper()

    init {
        viewBinderHelper.setOpenOnlyOne(true)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<OrderForm>() {
            override fun areItemsTheSame(oldItem: OrderForm, newItem: OrderForm): Boolean =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: OrderForm, newItem: OrderForm): Boolean =
                oldItem == newItem // 如果类没有声明为data这里会报错
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        viewBinderHelper.bind((holder as HomeViewHolder).swipeRevealLayout,getItem(position).id.toString())
        (holder as HomeViewHolder).bind(getItem(position))
    }
}