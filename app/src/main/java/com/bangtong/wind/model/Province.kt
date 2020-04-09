package com.bangtong.wind.model

import com.contrarywind.interfaces.IPickerViewData

data class Province(
    val city: List<City>,
    val name: String
):IPickerViewData{
    override fun getPickerViewText(): String {
        return name
    }
}



data class City(
    val area: List<String>,
    val name: String
)