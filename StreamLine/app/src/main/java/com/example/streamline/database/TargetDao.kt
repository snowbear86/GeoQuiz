package com.example.streamline.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.*
import com.example.streamline.Target

private const val TAG = "addButton"


@Dao
interface TargetDao {

    @Query("SELECT * FROM target")
    fun getTargets(): LiveData<List<Target>>



    @Query("SELECT * FROM target WHERE id=(:id)")
    fun getTarget(id: UUID): LiveData<Target?>

    @Update
    fun updateTarget(target: Target)

    @Insert
    fun addTarget(target: Target)


}
