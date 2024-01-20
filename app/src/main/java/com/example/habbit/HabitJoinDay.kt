package com.example.habbit

import androidx.room.*

@Entity(tableName = "habitJoinDay",
        primaryKeys = ["habitId", "dayId"],
        foreignKeys = [ForeignKey(entity = HabitBase::class, parentColumns = ["habitId"], childColumns = ["habitId"], onDelete = ForeignKey.CASCADE),
                        ForeignKey(entity = DayBase::class, parentColumns = ["dayId"], childColumns = ["dayId"], onDelete = ForeignKey.CASCADE)])

data class HabitJoinDay(//@PrimaryKey(autoGenerate = true) var id: Long?,
                        @ColumnInfo(name = "habitId") var habitId: Long,
                        @ColumnInfo(name = "dayId") var dayId: Long,
                        //@Ignore var dayId: Long,
                        @ColumnInfo(name = "name") var name: String,
                        //@ColumnInfo(name = "tracking") var tracking: Boolean,
                        @ColumnInfo(name = "color") var color: String,
                        @ColumnInfo(name = "streak") var streak: Int,
                        //@ColumnInfo(name = "reset_mask") var resetMask: Boolean,
                        @ColumnInfo(name = "day") var day: String)

