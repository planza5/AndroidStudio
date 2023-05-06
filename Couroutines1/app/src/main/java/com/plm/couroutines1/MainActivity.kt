package com.plm.couroutines1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.plm.couroutines1.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class MainActivity : AppCompatActivity() {
    lateinit var job:Job
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)

        job= MainScope().launch {
            for(i in 0..100){
                Log.d("PABLO","hilo "+i)
            }
        }

        binding.button.setOnClickListener(
            View.OnClickListener {
                if(job.isCompleted){
                    Log.d("PABLO","HOLA!!!!!")
                }
            }
        )
    }


}