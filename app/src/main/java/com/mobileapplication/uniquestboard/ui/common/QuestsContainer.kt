package com.mobileapplication.uniquestboard.ui.common

import android.util.Log
import androidx.fragment.app.Fragment
import com.mobileapplication.uniquestboard.Quest
import com.mobileapplication.uniquestboard.Status

public enum class ContainerAction{
    GetByPublisher,
    GetByTaker,
    GetByCurStatus
}
open class QuestsContainer:Fragment() {
    val TAG = "QuestsContainer"
    val numOfQuestsPerGet:Int = 10;

//    fun getQuests(action:ContainerAction,publisherID:String?,receiverID:String?,curStatus:Status?):MutableList<Quest>{
//        //根据条件查找一定数量（numOfQuestsPerGet）的quest，并返回list
//        return when(action){
//            ContainerAction.GetByPublisher->{
//
//            }
//            ContainerAction.GetByTaker->{
//
//            }
//            ContainerAction.GetByCurStatus->{
//
//            }
//            else->{
//                Log.e(TAG,"Unknow Action!")
//            }
//        }
//    }
}