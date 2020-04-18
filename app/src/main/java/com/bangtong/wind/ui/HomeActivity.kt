package com.bangtong.wind.ui

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.bangtong.wind.R
import com.bangtong.wind.adapter.HomeAdapter
import com.bangtong.wind.api.NetworkControl
import com.bangtong.wind.data.TinyDBManager
import com.bangtong.wind.model.OrderForm
import com.bangtong.wind.util.LogUtil
import com.bangtong.wind.util.MyActivity
import com.bangtong.wind.util.ToastUtil
import com.bangtong.wind.view.HomeViewModel
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_log.toolbar
import kotlinx.android.synthetic.main.nav_header.*


class HomeActivity : MyActivity(){

    private val TAG = "HomeActivity_ZBT"
    val viewModel by viewModels<HomeViewModel>()

    private val newAddOrderActivityRequestCode = 1
    private val newScanActivityRequestCode = 2
    private var progressBarVisible = View.GONE
    private val waitAdapter = HomeAdapter()
    private val transitAdapter = HomeAdapter()
    private val completedAdapter = HomeAdapter()
    private var waitListEmpty = true
    private var transitListEmpty = true
    private var completedListEmpty = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        observeData()
        initListener()
        packageNull.visibility = View.GONE
        recyclerView.adapter = waitAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        progressBar.visibility = progressBarVisible
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                //Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                //if(result.contents.)
                MaterialDialog(this).show {
                    title(text = result.contents)
                    message(R.string.confirm_bind)
                    positiveButton(R.string.confirm){
                        viewModel.bindOrderBox(result.contents.toLong(),viewModel.orderId)
                    }
                    negativeButton(android.R.string.cancel)
                    cornerRadius(10f) // 角半径
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
        //super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode){
                newAddOrderActivityRequestCode -> {
                    data?.getParcelableExtra<OrderForm>("order")?.let {
                        viewModel.insertCloud(it)
                    }
                }
                newScanActivityRequestCode -> {
                    ToastUtil.showLong(data!!.getStringExtra("code")!!)
                }
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
                circleImageView.setOnClickListener{
                    viewModel.debugCount++
                    if (viewModel.debugCount>5){
                        viewModel.debugCount = 0
                        val intent = Intent(this,DebugActivity::class.java)
                        startActivity(intent)
                    }
                }
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

    private fun observeData(){
        viewModel.orderForms.observe(this, Observer {
            val tempList = viewModel.orderForms.value?: emptyList()
            val waitList:MutableList<OrderForm> = ArrayList()
            val transitList:MutableList<OrderForm> = ArrayList()
            val completedList:MutableList<OrderForm> = ArrayList()
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
            waitListEmpty = waitList.isEmpty()
            transitListEmpty = transitList.isEmpty()
            completedListEmpty = completedList.isEmpty()
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
    }

    private fun initListener(){
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navAddress -> {
                    val intent = Intent(this,AdsManagerActivity::class.java)
                    startActivity(intent)
                }
                R.id.bindBox -> {
                    drawerLayout.closeDrawers()
                    recyclerView.adapter = waitAdapter
                    packageNull.visibility = if (waitListEmpty) View.VISIBLE else View.GONE
                    buttonMenu.close(true)
                }
                R.id.transmit -> {
                    drawerLayout.closeDrawers()
                    recyclerView.adapter = transitAdapter
                    packageNull.visibility = if (transitListEmpty) View.VISIBLE else View.GONE
                    buttonMenu.close(true)
                }
                R.id.completed -> {
                    drawerLayout.closeDrawers()
                    recyclerView.adapter = completedAdapter
                    packageNull.visibility = if (completedListEmpty) View.VISIBLE else View.GONE
                    buttonMenu.close(true)
                }
                R.id.log_out -> {
                    TinyDBManager.logOut()
                    val intent = Intent(this,LogActivity::class.java)
                    startActivity(intent)
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
            packageNull.visibility = if (waitListEmpty) View.VISIBLE else View.GONE
            buttonMenu.close(true)
        }
        transmit.setOnClickListener {
            recyclerView.adapter = transitAdapter
            packageNull.visibility = if (transitListEmpty) View.VISIBLE else View.GONE
            buttonMenu.close(true)
        }
        completed.setOnClickListener {
            recyclerView.adapter = completedAdapter
            packageNull.visibility = if (completedListEmpty) View.VISIBLE else View.GONE
            buttonMenu.close(true)
        }
        swipeRefresh.setOnRefreshListener{
            viewModel.syncOrder()
            swipeRefresh.isRefreshing = false
        }
    }

}

