package com.example.habbit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.*
import java.util.ArrayList
import kotlin.math.roundToLong

class ItemScreenViewModel (
    val dataSourceJoined: HabitJoinDayDAO,
    val dataSource: HabitDAO,
    application: Application): AndroidViewModel(application){

    var selectedItemId : Long? = 0L
    var firstEntry : String = ""
    var allEntries : Float = 0f
    var monthEntries : Int = 0
    var whichMonth : String = ""
    var monthCounter : Float = 1f
    var monthCounterLineChart : Int = 0
    var monthAverage : Float = 0f
    var entryCounter : Int = 0
    var lineArray = ArrayList <Entry>()
    var labelArray = ArrayList <String>()

    private val viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val scope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun deleteItem(id: Long) {
        scope.launch {
            delete(id)
        }
    }

    private suspend fun delete(id: Long) {
        withContext(Dispatchers.IO) {
            dataSource.deleteFromBase(id)
        }
    }

    fun getJoinedData(id: Long, date:String){
        var joinedList : MutableList<HabitJoinDay> = dataSourceJoined.getJoinedTablesForId(id)

        if (joinedList.size != 0) {
        labelArray.add(joinedList[0].day.substring(3,5)+"."+joinedList[0].day.substring(8,10))
            firstEntry = joinedList[0].day
            whichMonth = joinedList[0].day.substring(3)
            joinedList.forEach {
                allEntries++
                if (date.substring(7).equals(it.day.substring(3))) {
                    monthEntries++
                }
                if (!whichMonth.equals(it.day.substring(3))) {
                    monthCounter++
                    whichMonth = it.day.substring(3)
                    lineArray.add(Entry(monthCounterLineChart.toFloat(), entryCounter.toFloat()))
                    labelArray.add(it.day.substring(3,5)+"."+it.day.substring(8,10))
                    entryCounter = 1
                    monthCounterLineChart++
                }
                else {
                    entryCounter++
                }
            }
            lineArray.add(Entry(monthCounterLineChart.toFloat(), entryCounter.toFloat()))

            labelArray.forEach{println(it)}

            monthAverage = allEntries / monthCounter
        }
        else{
            firstEntry = date.substring(4)
            allEntries = 0f
            monthEntries = 0
            monthAverage = 0f
        }

    }
}