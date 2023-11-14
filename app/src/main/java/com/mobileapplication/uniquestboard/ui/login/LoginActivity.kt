package com.mobileapplication.uniquestboard.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobileapplication.uniquestboard.databinding.LoginBinding
import com.mobileapplication.uniquestboard.R

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}