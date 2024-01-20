package com.example.habbit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habitData")

data class HabitBase(@PrimaryKey(autoGenerate = true) var habitId: Long?,
                     @ColumnInfo(name = "name") var name: String,
                     @ColumnInfo(name = "tracking") var tracking: Boolean,
                     @ColumnInfo(name = "color") var color: String,
                     @ColumnInfo(name = "streak") var streak: Int,
                     @ColumnInfo(name = "reset_mask") var resetMask: Boolean)