package com.example.streamline

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.streamline.database.TargetDatabase
import java.util.*
import java.util.concurrent.Executors



private const val DATABASE_NAME = "target-database"


class TargetRepository private constructor(context: Context) {

    private val database : TargetDatabase = Room.databaseBuilder(
        context.applicationContext,
        TargetDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val targetDao = database.targetDao()
    private val executor = Executors.newSingleThreadExecutor()


    fun getTargets(): LiveData<List<Target>> = targetDao.getTargets()

    fun getTarget(id: UUID): LiveData<Target?> = targetDao.getTarget(id)

    fun updateTarget(target: Target) {
        executor.execute {
            targetDao.updateTarget(target)
        }
    }

    fun addTarget(target: Target) {
        executor.execute {
            targetDao.addTarget(target)
        }
    }

    companion object {
        private var INSTANCE: TargetRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TargetRepository(context)
            }
        }

        fun get(): TargetRepository {
            return INSTANCE ?:
            throw IllegalStateException("TargetRepository must be initialized")
        }
    }
}