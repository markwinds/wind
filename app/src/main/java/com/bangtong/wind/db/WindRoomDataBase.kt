package com.bangtong.wind.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bangtong.wind.model.OrderForm
import com.bangtong.wind.model.UserAddress
import com.bangtong.wind.util.MyApplication


@Database(entities = [UserAddress::class, OrderForm::class],version = 1,exportSchema = false)
//@TypeConverters(Converters::class)
public abstract class WindRoomDataBase: RoomDatabase(){
    abstract fun addressDao():AddressDao // 编译时根据返回类型WordDao判定该方法用于产生WordDao类
    abstract fun orderFormDao():OrderFormDao

    companion object {
        @Volatile
        private var INSTANCE: WindRoomDataBase? = null // 将最后生成的数据库对象存放在INSTANCE中
        fun getDatabase() =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    MyApplication.context,
                    WindRoomDataBase::class.java, // 传入数据库的类
                    "wind_database" // 传入数据库的名字
                ).build()
                INSTANCE = instance
                instance
            }
    }
}