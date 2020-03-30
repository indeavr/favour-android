package com.example.allwork.services.authentication

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.allfavour.data.AuthRepository
import com.example.allfavour.services.authentication.FavourAccountAuthenticator

class AuthenticationService : Service() {
    private lateinit var authenticator: FavourAccountAuthenticator

    override fun onCreate() {
        super.onCreate()
        authenticator = FavourAccountAuthenticator(this, application, AuthRepository())
    }

    override fun onBind(intent: Intent?): IBinder? {
        return authenticator.iBinder
    }

}