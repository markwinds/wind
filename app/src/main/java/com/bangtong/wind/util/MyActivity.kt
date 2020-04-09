package com.bangtong.wind.util

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

open class MyActivity : AppCompatActivity() {

    companion object{
        var activityStack = ArrayList<MyActivity>()
        fun destroyAllActivity(){
            for (a in activityStack){
                if (!a.isFinishing){
                    a.finish()
                }
            }
            activityStack.clear()
        }
        fun getTopActivity() = activityStack[activityStack.size-1]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityStack.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        activityStack.remove(this)
    }
}
