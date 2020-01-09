package com.example.allfavour.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.allfavour.R

class ConsumerActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumer)

        toolbar = findViewById(R.id.toolbar_consumer)
        setSupportActionBar(toolbar)
    }
}
