package com.bangtong.wind.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.bangtong.wind.R
import com.bangtong.wind.data.TinyDBManager
import com.bangtong.wind.util.MyActivity
import com.bangtong.wind.util.ToastUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_log.*
import kotlinx.android.synthetic.main.activity_log.toolbar
import kotlinx.android.synthetic.main.nav_header.*

class HomeActivity : MyActivity() {

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
                }else -> {
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
        addOrder.setOnClickListener{
            val intent = Intent(this,AddOrderActivity::class.java)
            startActivity(intent)
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
        }
        return true
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers()
            return
        }
        super.onBackPressed()
        MyActivity.destroyAllActivity()
    }
}
