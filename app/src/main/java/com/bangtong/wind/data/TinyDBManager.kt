package com.bangtong.wind.data

import com.bangtong.wind.util.MyApplication
import com.bangtong.wind.util.TinyDB

class TinyDBManager {

    companion object{
        private val tinyDB = TinyDB(MyApplication.context)
        var id: String = tinyDB.getString("userId")
        var pwd:String = tinyDB.getString("userPwd")

        fun skip():Boolean{
            id = tinyDB.getString("userId")
            pwd = tinyDB.getString("userPwd")
            return tinyDB.getString("skip") == "OK"
        }

        fun saveData(id:String,pwd:String){
            tinyDB.putString("userId",id)
            tinyDB.putString("userPwd",pwd)
            tinyDB.putString("skip","OK")
        }

        fun logOut(){
            tinyDB.putString("skip","NO")
        }
    }


}