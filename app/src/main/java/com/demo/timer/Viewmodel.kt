package com.demo.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class Viewmodel : ViewModel() {
    private var sleepTimerJob: Job? = null
    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String> = _timerText

    var min5: Boolean = false
    var min10: Boolean = false
    var min20: Boolean = false
    var min30: Boolean = false
    var min60: Boolean = false
    var min120: Boolean = false
    var min180: Boolean = false


    fun startSleepTimer(durationMs: Long) {
        stopSleepTimer()
//        sleepTimerJob = CoroutineScope(Dispatchers.Main).launch {
        sleepTimerJob = viewModelScope.launch {
            val endTime = System.currentTimeMillis() + durationMs
            while (System.currentTimeMillis() < endTime) {
                val remainingTime = endTime - System.currentTimeMillis()
                val hours = (remainingTime / (1000 * 60 * 60)).toInt()
                val minutes = ((remainingTime / (1000 * 60)) % 60).toInt()
                val seconds = ((remainingTime / 1000) % 60).toInt()

                _timerText.value = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                delay(1000) // Update every second
            }

            _timerText.value = "00:00:00" // Timer expired
            exitProcess(1)
        }
    }

    fun stopSleepTimer() {
        sleepTimerJob?.cancel()
        _timerText.value = "00:00:00" // Reset timer text
    }

    override fun onCleared() {
        super.onCleared()
        sleepTimerJob?.cancel() // Cancel job when ViewModel is cleared
    }
}
