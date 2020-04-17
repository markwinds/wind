package com.bangtong.wind.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bangtong.wind.R
import com.bangtong.wind.api.NetworkControl
import com.bangtong.wind.model.GoogleAddress
import com.bangtong.wind.model.OrderForm
import com.bangtong.wind.util.LogUtil
import com.bangtong.wind.util.MyActivity
import com.bangtong.wind.view.TransmitOrderModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_transmit_order.*


class TransmitOrderActivity : MyActivity(){

    companion object{
        val googleAddress: MutableLiveData<GoogleAddress> = MutableLiveData()
    }

    private val viewModel by viewModels<TransmitOrderModel>()
    private val map = SupportMapFragment()
    private val chart = ChartFragment()
    //private val markerOptions = MarkerOptions().position(LatLng(-33.852, 151.211)).title("2333")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transmit_order)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        viewModel.initData(intent.getParcelableExtra<OrderForm>("order")!!)
        viewModel.getBoxIfoCloud()
        showMap()
        viewModel.boxIfo.observe(this, Observer {
            updateData()
        })
        progressBar.visibility = View.VISIBLE
        NetworkControl.callBackComplete.observe(this, Observer {
            when(it){
                "Hide progress bar" -> progressBar.visibility = View.GONE
                else -> progressBar.visibility = View.VISIBLE
            }
        })
    }

    private fun updateData(){
        map.getMapAsync{
            val list = viewModel.boxIfo.value?: emptyList()
            if (list.isNotEmpty()){
                for (index in 0..(list.size-2)){
                    it.addPolyline(PolylineOptions().add(LatLng(list[index].x,list[index].y),LatLng(list[index+1].x,list[index+1].y)))
                }
                val start = MarkerOptions().position(LatLng(list[0].x, list[0].y))
                    .title(getString(R.string.starting))
                val now = MarkerOptions().position(LatLng(list.last().x,list.last().y))
                    .title(getString(R.string.now_location))
                it.addMarker(start)
                it.addMarker(now)
                it.moveCamera(CameraUpdateFactory.newLatLng(LatLng(list.last().x,list.last().y)))
            }
        }
        val list = viewModel.boxIfo.value?: emptyList()
        if (list.isNotEmpty()){
            chart.list = list
        }
    }

    private fun showMap(){
        updateData()
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,map).commit()
    }

    private fun showChart(){
        updateData()
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,chart).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
            R.id.location -> {
                showMap()
            }
            R.id.temperature -> {
                showChart()
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.transmit,menu)
        return true
    }
}
