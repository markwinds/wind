package com.bangtong.wind.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangtong.wind.R
import com.bangtong.wind.adapter.AdsManagerAdapter
import com.bangtong.wind.adapter.AdsManagerViewHolder
import com.bangtong.wind.api.NetworkControl
import com.bangtong.wind.data.TinyDBManager
import com.bangtong.wind.model.UserAddress
import com.bangtong.wind.util.LogUtil
import com.bangtong.wind.util.MyActivity
import com.bangtong.wind.view.AdsManagerViewModel
import kotlinx.android.synthetic.main.activity_ads_manager.*
import java.util.*

class AdsManagerActivity : MyActivity() {

    val viewModel by viewModels<AdsManagerViewModel>()
    private val adapterGone = AdsManagerAdapter(false)
    private val adapterShow = AdsManagerAdapter(true)
    private val editAddressActivityRequestAdd = 1
    private val editAddressActivityRequestEdit = 2
    private val TAG = "AdsManagerActivity_ZBT"
    private var processBarVisible = View.GONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads_manager)
        setSupportActionBar(toolbar3)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        recyclerView.adapter = adapterGone
        recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel.addresses.observe(this, Observer {
            adapterGone.submitList(it)
            adapterShow.submitList(it)
        })
        swipeRefresh.setOnRefreshListener {
            viewModel.syncCloud {
                swipeRefresh.isRefreshing = false
            }
        }
        NetworkControl.callBackComplete.observe(this, Observer {
            when(NetworkControl.callBackComplete.value){
                "Hide progress bar" -> {
                    if(progressBar == null){
                        processBarVisible = View.GONE
                        return@Observer
                    }
                    progressBar.visibility = View.GONE
                    LogUtil.d(TAG,"Hide progress bar")
                }
                "Show progress bar" -> {
                    if(progressBar == null){
                        processBarVisible = View.VISIBLE
                        return@Observer
                    }
                    progressBar.visibility = View.VISIBLE
                    LogUtil.d(TAG,"Show progress bar")
                }
            }
        })
        progressBar.visibility = View.GONE
    }

    fun goEditAddressActivity(address: UserAddress){
        val intent = Intent(this@AdsManagerActivity, EditAddressActivity::class.java)
        intent.putExtra("address",address)
        startActivityForResult(intent, editAddressActivityRequestEdit)
    }

    fun goAddOrderActivity(address:UserAddress){
        if(intent.getIntExtra("AddOrderActivity",0) != 0){
            val replyIntent = Intent().putExtra("address",address)
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.edit -> {
                recyclerView.adapter = if(recyclerView.adapter == adapterGone) adapterShow else adapterGone
            }
            R.id.add -> {
                val intent = Intent(this@AdsManagerActivity, EditAddressActivity::class.java)
                startActivityForResult(intent, editAddressActivityRequestAdd)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == editAddressActivityRequestAdd && resultCode == Activity.RESULT_OK) {
            LogUtil.d(TAG,"In activity result add")
            data?.getParcelableExtra<UserAddress>("address")?.let {
                it.id = Random().nextLong()
                viewModel.insertCloud(it)
            }
        }
        if (requestCode == editAddressActivityRequestEdit && resultCode == Activity.RESULT_OK) {
            LogUtil.d(TAG,"In activity result edit")
            data?.getParcelableExtra<UserAddress>("address")?.let {
                viewModel.updateCloud(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ads_manager,menu)
        return true
    }
}
