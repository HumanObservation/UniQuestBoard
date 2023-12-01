package com.mobileapplication.uniquestboard.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobileapplication.uniquestboard.R
import com.mobileapplication.uniquestboard.databinding.RegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}