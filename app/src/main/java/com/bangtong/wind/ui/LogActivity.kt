package com.bangtong.wind.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bangtong.wind.R
import com.bangtong.wind.data.TinyDBManager
import com.bangtong.wind.util.MyActivity
import com.bangtong.wind.util.MyApplication
import com.bangtong.wind.util.TinyDB
import com.bangtong.wind.view.LogViewModel
import kotlinx.android.synthetic.main.activity_log.*

class LogActivity : MyActivity() {

    private val viewModel by viewModels<LogViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        setSupportActionBar(toolbar)
        if(TinyDBManager.skip()){
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }
        userId.setText(TinyDBManager.id)
        userPwd.setText(TinyDBManager.pwd)
        buttonLogIn.setOnClickListener{
            if(userId.text.toString() != "" && userPwd.text.toString() != "") {
                viewModel.logIn(userId.text.toString(), userPwd.text.toString())
            }
        }
        buttonSignUp.setOnClickListener{
            if(userId.text.toString() != "" && userPwd.text.toString() != "")
                viewModel.signUp(userId.text.toString(),userPwd.text.toString())
        }
        viewModel.logState.observe(this, Observer {
            if(it == "OK"){
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
            }
        })
    }


}
