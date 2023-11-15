package com.mobileapplication.uniquestboard.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.mobileapplication.uniquestboard.databinding.LoginBinding
import com.mobileapplication.uniquestboard.R

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var lbtn : Button = binding.login;
        var rbtn : Button = binding.register;
        var et1 : EditText = binding.itsc;
        var et2 : EditText = binding.password;

        lbtn.setOnClickListener{
            var itsc : String = et1.text.toString();
            var pw : String = et2.text.toString();

        }
    }
}