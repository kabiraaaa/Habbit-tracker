package com.example.habbit

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HabitBase::class, DayBase::class, HabitJoinDay::class], version = 2)

abstract class HabitDatabase: RoomDatabase(){
    abstract val dayDAO: DayDAO
    abstract val habitDAO: HabitDAO
    abstract val joinedDAO: HabitJoinDayDAO

    companion object {

        @Volatile
        private var INSTANCE: HabitDatabase? = null

        fun getInstance(context: Context): HabitDatabase? {
            if (this.INSTANCE == null) {
                synchronized(HabitDatabase::class) {
                    this.INSTANCE =
                        Room.databaseBuilder(context.applicationContext, HabitDatabase::class.java, "habbit").allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            return this.INSTANCE
        }
        fun destroyInstance(){
            this.INSTANCE = null
        }
    }
}