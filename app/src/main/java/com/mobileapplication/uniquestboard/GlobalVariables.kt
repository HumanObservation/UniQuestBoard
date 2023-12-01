package com.mobileapplication.uniquestboard

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter

object GlobalVariables{
    var user : User = User("tester","123123");
    var ip : String = "10.89.120.87"
    var port:Int = 8080
    @RequiresApi(Build.VERSION_CODES.O)
    val df = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")

}