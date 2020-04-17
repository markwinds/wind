package com.bangtong.wind.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangtong.wind.R
import com.bangtong.wind.view.DebugViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_debug.*

class DebugActivity : AppCompatActivity() {

    private val viewModel by viewModels<DebugViewModel>()
    private val qrCodeFragment = QRCodeFragment()
    private val scLockFragment = SCLockFragment()
    private val scDataFragment = SCDataFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        viewPager.adapter = object :FragmentStateAdapter(this){
            override fun getItemCount()=3
            override fun createFragment(position: Int): Fragment =
                when(position){
                    0 -> qrCodeFragment
                    1 -> scLockFragment
                    else -> scDataFragment
                }
        }
        TabLayoutMediator(tabLayout,viewPager){ tab, position ->
            when(position){
                0 -> tab.text = "QRCode"
                1 -> tab.text = "Locker"
                else -> tab.text = "Data"
            }
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}
