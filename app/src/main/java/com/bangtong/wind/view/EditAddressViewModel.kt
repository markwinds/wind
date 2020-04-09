package com.bangtong.wind.view

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangtong.wind.R
import com.bangtong.wind.model.Province
import com.bangtong.wind.util.GetJsonDataUtil
import com.bangtong.wind.util.LogUtil
import com.bangtong.wind.util.MyActivity
import com.bangtong.wind.util.MyApplication
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EditAddressViewModel:ViewModel() {

    val TAG = "EditAddressViewModel_ZBT"
    private var provinces: MutableList<Province> = ArrayList()
    private val cities: MutableList<MutableList<String>> = ArrayList()
    private val areas: MutableList<MutableList<MutableList<String>>> = ArrayList()
    val area = MutableLiveData<MutableList<String>>()

    init {
        loadFromJson()
    }

    private fun loadFromJson(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val data = GetJsonDataUtil.getJson(MyApplication.context, "province.json").replace(" ","")
                provinces = Gson().fromJson(data, Array<Province>::class.java).toMutableList()
                for (p in provinces){
                    val pc:MutableList<String> = ArrayList() // 每个省的城市名列表
                    val pa:MutableList<MutableList<String>> = ArrayList() // 每个省的地区名列表
                    for(c in p.city){
                        pc.add(c.name)
                        val ca:MutableList<String> = ArrayList() // 每个城市的地区列表
                        for (a in c.area){
                            ca.add(a)
                        }
                        pa.add(ca)
                    }
                    cities.add(pc)
                    areas.add(pa)
                }
            }
        }
    }

    fun showProvincePicker(){
        val picker = OptionsPickerBuilder(MyActivity.getTopActivity(),
            OnOptionsSelectListener{p1,p2,p3,_ ->
                val temp:MutableList<String> = ArrayList()
                temp.add(provinces[p1].name)
                temp.add(provinces[p1].city[p2].name)
                temp.add(provinces[p1].city[p2].area[p3])
                area.postValue(temp)
        }).setTitleText(MyActivity.getTopActivity().getString(R.string.choose_location))
            .setSubmitText(MyActivity.getTopActivity().getString(R.string.submit))//确定按钮文字
            .setCancelText(MyActivity.getTopActivity().getString(R.string.cancle))//取消按钮文字
            .setOutSideCancelable(false)
            .isDialog(true)
            .build<Any>()
        picker.setPicker(provinces as List<Province>,cities as List<List<String>>,areas as List<List<List<String>>>)
        picker.show()
    }
}