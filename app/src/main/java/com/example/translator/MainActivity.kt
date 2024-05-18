package com.example.translator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.domain.Testtest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    val test = Testtest("asdasd")
}