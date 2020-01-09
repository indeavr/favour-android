package com.example.allfavour

import PositionsQuery
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.allfavour.graphql.GraphqlConnector
import com.example.allfavour.ui.ConsumerActivity
import com.example.allfavour.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GraphqlConnector.setup(applicationContext)

//        val loginIntent = Intent(this, LoginActivity::class.java)
//
//        startActivity(loginIntent)

        val consumerActivityIntent = Intent(this, ConsumerActivity::class.java)
        startActivity(consumerActivityIntent);
    }
}
