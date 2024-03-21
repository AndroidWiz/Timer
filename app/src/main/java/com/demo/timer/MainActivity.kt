package com.demo.timer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.demo.timer.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: Viewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[Viewmodel::class.java]

        viewModel.timerText.observe(this) { timerText ->
            binding.tvTimer.text = timerText
        }

        binding.tvGo.setOnClickListener {
            Intent(this, MainActivity2::class.java).also { startActivity(it) }
        }
        binding.btnAdd.setOnClickListener {
            val timer =
                viewModel.min5 || viewModel.min10 || viewModel.min20 || viewModel.min30 || viewModel.min60 || viewModel.min120 || viewModel.min180
            if (!timer) {
                showSleepTimer()
            } else {
                showStopTimerDialog()
            }
        }
    }

    private fun showSleepTimer() {
        val bSDialog = BottomSheetDialog(this)
        val dialogView = layoutInflater.inflate(R.layout.sleep_timer, null)
        bSDialog.setContentView(dialogView)

        val timerOptions = listOf<Pair<Long, String>>(
            5L to "5 minutes",
            10L to "10 minutes",
            20L to "20 minutes",
            30L to "30 minutes",
            60L to "1 hour",
            120L to "2 hours",
            180L to "3 hours"
        )

        for ((time, label) in timerOptions) {
            dialogView.findViewById<TextView>(getTimerTextViewId(time)).setOnClickListener {
                startSleepTimer(time * 1000)
                shortSnack(binding.root, "Radio will stop playing in $label")

                when (time) {
                    5L -> viewModel.min5 = true
                    10L -> viewModel.min10 = true
                    20L -> viewModel.min20 = true
                    30L -> viewModel.min30 = true
                    60L -> viewModel.min60 = true
                    120L -> viewModel.min120 = true
                    180L -> viewModel.min180 = true
                }

                bSDialog.dismiss()
            }
        }

        bSDialog.setCanceledOnTouchOutside(false)
        bSDialog.show()
    }

    private fun getTimerTextViewId(minutes: Long): Int {
        return when (minutes) {
            5L -> R.id.tv_5_min
            10L -> R.id.tv_10_min
            20L -> R.id.tv_20_min
            30L -> R.id.tv_30_min
            60L -> R.id.tv_1_hour
            120L -> R.id.tv_2_hour
            180L -> R.id.tv_3_hour
            else -> throw IllegalArgumentException("Invalid sleep timer option")
        }
    }

    private fun startSleepTimer(durationMs: Long) {
        viewModel.startSleepTimer(durationMs)
    }

    private fun stopSleepTimer() {
        viewModel.stopSleepTimer()
    }

    private fun showStopTimerDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Stop Timer")
            .setMessage("Are you sure you want to stop the timer?")
            .setPositiveButton("Yes") { _, _ ->
                stopSleepTimer()
                viewModel.min5 = false
                viewModel.min10 = false
                viewModel.min20 = false
                viewModel.min30 = false
                viewModel.min60 = false
                viewModel.min120 = false
                viewModel.min180 = false
                shortSnack(binding.root, "Sleep timer stopped")
                binding.tvTimer.text = "Timer not set yet."
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val customDialog = builder.create()
        customDialog.show()
    }

    private fun shortSnack(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(view, message, duration).show()
    }
}