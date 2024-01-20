package com.example.habbit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ItemScreenViewModelFactory(
    private val dataSourceJoined: HabitJoinDayDAO,
    private val dataSource: HabitDAO,
    private val application: Application): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemScreenViewModel::class.java)){
            return ItemScreenViewModel(dataSourceJoined, dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}