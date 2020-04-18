package com.bangtong.wind.util

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.reflect.KClass

open class MyActivity : AppCompatActivity() {

    companion object{
        var activityStack = ArrayList<ActivityStackCell>()
        fun destroyAllActivity(){
            for (a in activityStack){
                if (!a.activity.isFinishing){
                    a.activity.finish()
                }
            }
            activityStack.clear()
        }
        fun getTopActivity() = activityStack[activityStack.size-1].activity
        fun getTopActivityWithType(type:String):MyActivity{
            var index = activityStack.size-1
            while (index>0 && activityStack[index].type != type){
                index--
            }
            return activityStack[index].activity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityStack.add(ActivityStackCell(this,this::class.qualifiedName!!.split(".").last()))
    }

    override fun onDestroy() {
        super.onDestroy()
        activityStack.remove(ActivityStackCell(this,this::class.qualifiedName!!.split(".").last()))
    }

    inner class ActivityStackCell(val activity:MyActivity,val type: String)
}
