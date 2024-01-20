package com.example.habbit

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel (
    val dataSourceHabit: HabitDAO,
    val dataSourceDay: DayDAO,
    val dataBase: HabitDatabase,
    application: Application): AndroidViewModel(application){

    private lateinit var writableCurrentDay: String
    var habitList : LiveData<List<HabitBase>> = dataSourceHabit.selectAllHabit()
    private val _currentDay = MutableLiveData<String>()
    val currentDay : LiveData<String>
        get() = _currentDay

    init {
        _currentDay.value = getDateVal()
        writableCurrentDay = _currentDay.value.toString().substring(4)
        println(writableCurrentDay)
    }

    private val viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val scope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun deleteAll()
    {
        scope.launch{
            delete()
        }
    }

    private suspend fun delete(){
        withContext(Dispatchers.IO){
            dataBase.clearAllTables()
        }
    }

    fun onBoxChecking(id: Long) {
        scope.launch{
            onBoxCheckingUpdate(id)
        }
    }

    private suspend fun onBoxCheckingUpdate(id: Long) {
        withContext(Dispatchers.IO){
            dataSourceHabit.updateChecked(id)
            dataSourceDay.insertDay(DayBase(null, id, writableCurrentDay))
        }
    }

    fun onBoxUnchecking(id: Long) {
        scope.launch{
            onBoxUncheckingUpdate(id)
        }
    }

    private suspend fun onBoxUncheckingUpdate(id: Long) {
        withContext(Dispatchers.IO){
            dataSourceHabit.updateUnchecked(id)
            dataSourceDay.deleteFromDayBase(id, writableCurrentDay)
        }
    }

    fun dayChanged() {
        scope.launch{
            _dayChanged()
        }
    }

    private suspend fun _dayChanged() {
        withContext(Dispatchers.IO) {
            dataSourceHabit.updateStreakNewDay()
            dataSourceHabit.updateMaskNewDay()
        }
    }

    private fun getDateVal(): String{
        val currentDate: Date = Calendar.getInstance().time
        val locale: Locale = Locale.getDefault()
        val format: String = "EEE dd.MM.yyyy"
        return SimpleDateFormat(format,locale).format(currentDate)
    }
}