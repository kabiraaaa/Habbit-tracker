package com.example.habbit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "dayData",
    foreignKeys = [ForeignKey(
        entity = HabitBase::class,
        parentColumns = ["habitId"],
        childColumns = ["habit_id"],
        onDelete = ForeignKey.CASCADE
    )]
)

data class DayBase(
    @PrimaryKey(autoGenerate = true) var dayId: Long?,
    @ColumnInfo(name = "habit_id") var habitId: Long?,
    @ColumnInfo(name = "day") var day: String
)