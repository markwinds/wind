package com.bangtong.wind.view

import androidx.lifecycle.ViewModel
import com.bangtong.wind.data.WindRepository

class HomeViewModel:ViewModel() {

    private val windRepository = WindRepository()
    private val orderForms = windRepository.getAllOrder()

}