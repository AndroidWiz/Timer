package com.demo.timer

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.demo.timer.databinding.ActivityMain2Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var viewModel: Viewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[Viewmodel::class.java]

        /* viewModel.timerText.observe(this) { timerText ->
             Log.d("MainActivity2", "TimerText updated: $timerText")
             binding.tvTimer.text = timerText
         }*/

        val timerText = viewModel.timerText.value
        binding.tvTimer.text = timerText
        Log.d("MainActivity2", "TimerText updated: $timerText")
    }

}