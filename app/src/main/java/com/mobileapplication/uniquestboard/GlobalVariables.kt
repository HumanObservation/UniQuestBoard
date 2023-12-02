package com.mobileapplication.uniquestboard

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter

object GlobalVariables{
    var user : User = User("tester","123123");
    var ip : String = "192.168.36.233"
    var port:Int = 8080
    @RequiresApi(Build.VERSION_CODES.O)
    val df = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")

}