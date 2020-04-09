package com.bangtong.wind.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangtong.wind.R
import com.bangtong.wind.adapter.AdsManagerAdapter
import com.bangtong.wind.adapter.AdsManagerViewHolder
import com.bangtong.wind.data.TinyDBManager
import com.bangtong.wind.model.UserAddress
import com.bangtong.wind.util.LogUtil
import com.bangtong.wind.util.MyActivity
import com.bangtong.wind.view.AdsManagerViewModel
import kotlinx.android.synthetic.main.activity_ads_manager.*
import java.util.*

class AdsManagerActivity : MyActivity() {

    private val viewModel by viewModels<AdsManagerViewModel>()
    private val adapterGone = AdsManagerAdapter(false)
    private val adapterShow = AdsManagerAdapter(true)
    private val editAddressActivityRequestCode = 1
    private val TAG = "AdsManagerActivity_ZBT"

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

        viewModel.test(UserAddress(11,TinyDBManager.id,"周邦统","13588781370","pr","c","d","d"))
        viewModel.insert(UserAddress(21,TinyDBManager.id,"周邦统","13588781370","pr","c","d","d"))
        viewModel.insert(UserAddress(31,TinyDBManager.id,"31","13588781370","pr","c","d","d"))
        viewModel.insert(UserAddress(41,TinyDBManager.id,"41","13588781370","pr","c","d","d"))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.edit -> {
                recyclerView.adapter = if(recyclerView.adapter == adapterGone) adapterShow else adapterGone
            }
            R.id.add -> {
                val intent = Intent(this@AdsManagerActivity, EditAddressActivity::class.java)
                startActivityForResult(intent, editAddressActivityRequestCode)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == editAddressActivityRequestCode && resultCode == Activity.RESULT_OK) {
            LogUtil.d(TAG,"In activity result")
            data?.getParcelableExtra<UserAddress>("address")?.let {
                it.id = Random().nextLong()
                viewModel.insert(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ads_manager,menu)
        return true
    }
}
