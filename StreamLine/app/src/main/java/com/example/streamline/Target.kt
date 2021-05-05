package com.example.streamline

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class Target (@PrimaryKey val id: UUID = UUID.randomUUID(),
                   var title: String = "",
                   var date: Date = Date(),
                   var details: String = "",
                   var isSolved: Boolean = false)



