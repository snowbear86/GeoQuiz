package com.example.streamline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*


class TargetDetailViewModel : ViewModel() {

    private val targetRepository = TargetRepository.get()
    private val targetIdLiveData = MutableLiveData<UUID>()

    var targetLiveData: LiveData<Target?> =
        Transformations.switchMap(targetIdLiveData) { targetId ->
            targetRepository.getTarget(targetId)
        }

    fun loadTarget(targetId: UUID) {
        targetIdLiveData.value = targetId
    }

    fun saveTarget(target: Target) {
        targetRepository.updateTarget(target)
    }
}