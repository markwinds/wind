package com.bangtong.wind.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangtong.wind.R
import com.bangtong.wind.adapter.HomeAdapter
import com.bangtong.wind.api.NetworkControl
import com.bangtong.wind.data.TinyDBManager
import com.bangtong.wind.model.OrderForm
import com.bangtong.wind.util.LogUtil
import com.bangtong.wind.util.MyActivity
import com.bangtong.wind.util.ScanActivity
import com.bangtong.wind.view.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_log.toolbar
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait


class HomeActivity : MyActivity() {

    private val TAG = "HomeActivity_ZBT"
    val viewModel by viewModels<HomeViewModel>()

    private val newAddOrderActivityRequestCode = 1
    private val newScanActivityRequestCode = 1
    private var progressBarVisible = View.GONE
    private val waitAdapter = HomeAdapter()
    private val transitAdapter = HomeAdapter()
    private val completedAdapter = HomeAdapter()
    private val waitList:MutableList<OrderForm> = ArrayList()
    private val transitList:MutableList<OrderForm> = ArrayList()
    private val completedList:MutableList<OrderForm> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navAddress -> {
                    val intent = Intent(this,AdsManagerActivity::class.java)
                    startActivity(intent)
                }
                R.id.bindBox -> {
                    drawerLayout.closeDrawers()
                    recyclerView.adapter = waitAdapter
                    packageNull.visibility = if (waitList.isEmpty()) View.VISIBLE else View.GONE
                }
                R.id.transmit -> {
                    drawerLayout.closeDrawers()
                    recyclerView.adapter = transitAdapter
                    packageNull.visibility = if (transitList.isEmpty()) View.VISIBLE else View.GONE
                }
                R.id.completed -> {
                    drawerLayout.closeDrawers()
                    recyclerView.adapter = completedAdapter
                    packageNull.visibility = if (completedList.isEmpty()) View.VISIBLE else View.GONE
                }
                else -> {
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
        addOrder.setOnClickListener{
            val intent = Intent(this,AddOrderActivity::class.java)
            startActivityForResult(intent,newAddOrderActivityRequestCode)
        }
        bindBox.setOnClickListener {
            recyclerView.adapter = waitAdapter
            packageNull.visibility = if (waitList.isEmpty()) View.VISIBLE else View.GONE
        }
        transmit.setOnClickListener {
            recyclerView.adapter = transitAdapter
            packageNull.visibility = if (transitList.isEmpty()) View.VISIBLE else View.GONE
        }
        completed.setOnClickListener {
            recyclerView.adapter = completedAdapter
            packageNull.visibility = if (completedList.isEmpty()) View.VISIBLE else View.GONE
        }
        swipeRefresh.setOnRefreshListener{
            viewModel.syncOrder()
            swipeRefresh.isRefreshing = false
        }
        packageNull.visibility = View.GONE
        recyclerView.adapter = waitAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel.orderForms.observe(this, Observer {
            val tempList = viewModel.orderForms.value?: emptyList()
            waitList.clear()
            transitList.clear()
            completedList.clear()
            for (order in tempList){
                when(order.boxId){
                    0.toLong() -> waitList.add(order)
                    (-1).toLong() -> completedList.add(order)
                    else -> transitList.add(order)
                }
            }
            waitAdapter.submitList(waitList)
            transitAdapter.submitList(transitList)
            completedAdapter.submitList(completedList)
        })
        NetworkControl.callBackComplete.observe(this, Observer {
            when(NetworkControl.callBackComplete.value){
                "Hide progress bar" -> {
                    if(progressBar == null){
                        progressBarVisible = View.GONE
                        return@Observer
                    }
                    progressBar.visibility = View.GONE
                }
                "Show progress bar" -> {
                    if(progressBar == null){
                        progressBarVisible = View.VISIBLE
                        return@Observer
                    }
                    progressBar.visibility = View.VISIBLE
                }
            }
        })
        progressBar.visibility = progressBarVisible
    }

    fun goScanActivity(){
        val intent = Intent(this,ScanActivity::class.java)
        startActivityForResult(intent,newScanActivityRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newAddOrderActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getParcelableExtra<OrderForm>("order")?.let {
                viewModel.insertCloud(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                userName.text = TinyDBManager.id
            }
            R.id.refresh -> {
                viewModel.syncOrder()
            }
        }
        return true
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers()
            return
        }
        //buttonMenu.hideMenuButton(true)
        if(buttonMenu.isOpened){
            buttonMenu.close(true)
            return
        }
        super.onBackPressed()
        MyActivity.destroyAllActivity()
    }

    fun goCompletedOrderActivity(order:OrderForm){
        val intent = Intent(this,CompletedOrderActivity::class.java)
        intent.putExtra("order",order)
        startActivity(intent)
    }
}
