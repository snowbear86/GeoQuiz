package com.example.streamline

import android.util.Log
import androidx.lifecycle.ViewModel
 private const val TAG = "addButton"

class TargetListViewModel : ViewModel() {


    private val targetRepository = TargetRepository.get()
    val targetListLiveData = targetRepository.getTargets()

    fun addTarget(target: Target) {
        Log.d(TAG, "ADD")
        targetRepository.addTarget(target)
    }
}