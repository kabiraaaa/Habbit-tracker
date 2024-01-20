package com.example.habbit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class MainViewModelFactory(
    private val dataSourceHabit: HabitDAO,
    private val dataSourceDay: DayDAO,
    private val dataBase: HabitDatabase,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dataSourceHabit, dataSourceDay, dataBase, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}