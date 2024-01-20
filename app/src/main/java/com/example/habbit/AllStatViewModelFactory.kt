package com.example.habbit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class AllStatViewModelFactory(
    private val dataSourceJoined: HabitJoinDayDAO,
    private val dataBase: HabitDatabase,
    private val application: Application): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllStatViewModel::class.java)){
            return AllStatViewModel(dataSourceJoined, dataBase, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}