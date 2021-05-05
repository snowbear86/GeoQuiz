package com.example.streamline.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.streamline.Target


@Database(entities = [ Target::class ], version=1, exportSchema = false)

@TypeConverters(TargetTypeConverters::class)

abstract class TargetDatabase : RoomDatabase() {

    abstract fun targetDao(): TargetDao

}