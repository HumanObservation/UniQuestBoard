package com.mobileapplication.uniquestboard.ui.common

import android.util.Log
import com.mobileapplication.uniquestboard.R
//TAG:String = "Utility"
fun getCardColor(status: Status):Int{
    return when(status){
        Status.PENDING, Status.IN_PROGRESS->return R.color.on_going_green;
        Status.COMPLETED-> R.color.completed_blue;
        Status.FAILED, Status.EXPIRED, Status.INTERRUPTED-> R.color.failed_red;
        else->
        {
            Log.e("Utility","No matching status!")
            return R.color.error_yellow;
        }
    }
}