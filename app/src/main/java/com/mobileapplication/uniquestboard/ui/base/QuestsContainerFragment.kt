package com.mobileapplication.uniquestboard.ui.base

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.mobileapplication.uniquestboard.ui.common.Contact
import com.mobileapplication.uniquestboard.ui.common.Quest
import com.mobileapplication.uniquestboard.ui.common.Status
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

public enum class ContainerAction{
    GetByPublisher,
    GetByTaker,
    GetByCurStatus
}
open class QuestsContainer:Fragment() {
    open val TAG = "QuestsContainer"
    open lateinit var action:ContainerAction
    val numOfQuestsPerGet:Int = 10;
    @RequiresApi(Build.VERSION_CODES.O)
    val df = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
    @RequiresApi(Build.VERSION_CODES.O)
    val df_dateOnly = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    @RequiresApi(Build.VERSION_CODES.O)
    val df_timeOnly = DateTimeFormatter.ofPattern("HH:mm")


    //variable under is just for test
    val taker = mutableListOf<String>("someone")
    var contact = Contact("55556666","@admin")
    var status = Status.FAILED;
    val image = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    val testQuest = Quest(
        LocalDateTime.now(),
        LocalDateTime.now(),
        "kami",
        taker,
        "the test title somehow the price is higher than expected 6666666666666666666666666666666666666666666666666666666666666666666" +
                "11111111111111111111111111111111111111111111111111111111111111111111111111111"
                ,
        "the test content tttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
                "555555555555555555555555555555555555555555555555555555555555555555555555555555555" +
                "888888888888888888888888888888888888888888888888888888888888888888888888888888888" +
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr",
        status,
        image,
        "thankfulness",
        contact
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getQuests(action: ContainerAction, publisherID:String?, receiverID:String?, curStatus: Status?):MutableList<Quest>{
        //根据条件查找一定数量（numOfQuestsPerGet）的quest，并返回list
        return when(action){
//            ContainerAction.GetByPublisher->{
//
//            }
//            ContainerAction.GetByTaker->{
//
//            }
//            ContainerAction.GetByCurStatus->{
//
//            }
            else->{
                Log.e(TAG,"Unknow Action!")
                var questList = mutableListOf<Quest>(testQuest)
                return questList
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getAQuest(questID: UUID): Quest {
        //get quest from database
        Log.d(TAG,"return the testQuest whose title is " + testQuest.title)
        return testQuest;
    }
}