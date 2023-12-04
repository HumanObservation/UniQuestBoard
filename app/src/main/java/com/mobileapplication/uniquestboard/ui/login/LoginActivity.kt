package com.mobileapplication.uniquestboard.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mobileapplication.uniquestboard.GlobalVariables
import com.mobileapplication.uniquestboard.MainActivity
import com.mobileapplication.uniquestboard.databinding.LoginBinding
import com.mobileapplication.uniquestboard.User
import com.mobileapplication.uniquestboard.ui.register.RegisterActivity
import org.json.JSONObject

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
        var url : String = "http://${GlobalVariables.ip}:${GlobalVariables.port}/android/DB_login.php";
        var rq = Volley.newRequestQueue(this);
        lbtn.setOnClickListener{
            var itsc : String = et1.text.toString();
            var pw : String = et2.text.toString();
            var sr = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    if(response.toString().trim() == "The record is found.")
                    {
                        Toast.makeText(applicationContext, "Login successes", Toast.LENGTH_SHORT).show();
                        GlobalVariables.user = User(itsc,pw)
                        Intent(this, MainActivity::class.java).also { startActivity(it); }
                    }
                    else
                    {
                        Toast.makeText(applicationContext, "Incorrect ITSC or Password", Toast.LENGTH_SHORT).show();
                    }},
                Response.ErrorListener { e -> Toast.makeText(applicationContext,
                    "Login fail.$e", Toast.LENGTH_SHORT).show() })
            {
                override fun getParams(): MutableMap<String, String>? {
                    var params = HashMap<String, String>();
                    params.put("itsc", itsc);
                    params.put("password", pw);
                    return params;
                }
            }
            rq.add(sr);
        }
        rbtn.setOnClickListener{
            Intent(this, RegisterActivity::class.java).also { startActivity(it) }
        }
    }
}