package com.mobileapplication.uniquestboard.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mobileapplication.uniquestboard.GlobalVariables
import com.mobileapplication.uniquestboard.MainActivity
import com.mobileapplication.uniquestboard.R
import com.mobileapplication.uniquestboard.User
import com.mobileapplication.uniquestboard.databinding.RegisterBinding
import com.mobileapplication.uniquestboard.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var lbtn : Button = binding.Login1;
        var rbtn : Button = binding.register1;
        var et1 : EditText = binding.itsc;
        var et2 : EditText = binding.password;
        var et3 :  EditText = binding.studentId;
        var et4 : EditText = binding.email;
        var url : String = "http://${GlobalVariables.ip}:${GlobalVariables.port}/android/DB_Register.php";
        var rq = Volley.newRequestQueue(this);
        rbtn.setOnClickListener{
            var itsc : String = et1.text.toString();
            var pw : String = et2.text.toString();
            var id : String = et3.text.toString();
            var email: String = et4.text.toString();
            var sr = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response ->
                    if(response.toString().trim() == "Data inserted successfully!")
                    {
                        Toast.makeText(applicationContext, "Register successes.", Toast.LENGTH_SHORT).show();
                        Intent(this, LoginActivity::class.java).also { startActivity(it); }
                    }},
                Response.ErrorListener { e -> Toast.makeText(applicationContext,
                    "Register fail.$e", Toast.LENGTH_SHORT).show() })
            {
                override fun getParams(): MutableMap<String, String>? {
                    var params = HashMap<String, String>();
                    params.put("student_id", id);
                    params.put("itsc", itsc);
                    params.put("email", email);
                    params.put("password", pw);
                    return params;
                }
            }
            rq.add(sr);
        }
        lbtn.setOnClickListener{
            Intent(this, LoginActivity::class.java).also { startActivity(it) }
        }
    }
}