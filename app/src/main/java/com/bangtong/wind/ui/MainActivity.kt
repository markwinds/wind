package com.bangtong.wind.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangtong.wind.R
import com.bangtong.wind.util.MyActivity

class MainActivity : MyActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
