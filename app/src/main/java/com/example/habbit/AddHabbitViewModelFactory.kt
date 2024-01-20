package com.example.habbit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class AddHabbitViewModelFactory(
    private val dataSource: HabitDAO,
    private val application: Application): ViewModelProvider.Factory{

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddHabbitViewModel::class.java)){
            return AddHabbitViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}