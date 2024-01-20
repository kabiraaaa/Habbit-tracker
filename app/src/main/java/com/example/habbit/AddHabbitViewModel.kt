package com.example.habbit

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class AddHabbitViewModel (
    val dataSource: HabitDAO,
    application: Application): AndroidViewModel(application){

    var color: Int = Color.parseColor("#008577")

    private val viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val scope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun addToDatabase(name: String, track: Boolean) {
        scope.launch {
            insert(name, track)
        }
    }
    private suspend fun insert(name: String, track: Boolean) {
        withContext(Dispatchers.IO) {
            dataSource.insertHabit(HabitBase(null,name,track,color.toString(),0,false))
            println(dataSource.selectAll())
        }
    }

    fun updateHabitItem(id: Long, name: String, tracking: Boolean) {
        scope.launch {
            update(id, name, tracking)
        }
    }

    private suspend fun update(id: Long, name: String, tracking: Boolean) {
        withContext(Dispatchers.IO) {
            dataSource.updateItem(id, name, tracking, color.toString())
        }
    }
}