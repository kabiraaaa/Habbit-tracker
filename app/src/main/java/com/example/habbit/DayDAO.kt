package com.example.habbit

import androidx.room.*

@Dao

interface DayDAO {

    @Insert
    fun insertDay(dayData: DayBase)

    @Update
    fun updateDay(dayData: DayBase)

    @Delete
    fun deleteDay(dayData: DayBase)

    @Query("delete from dayData where habit_id = :id and day like :day")
    fun deleteFromDayBase(id: Long, day: String)
}

