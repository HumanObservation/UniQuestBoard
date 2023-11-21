package com.mobileapplication.uniquestboard.ui.questPublish

import androidx.lifecycle.ViewModel
import com.mobileapplication.uniquestboard.ui.base.QuestContainerViewModel
import com.mobileapplication.uniquestboard.ui.common.Quest
import java.time.LocalDateTime

class QuestPublishViewModel : QuestContainerViewModel() {
    var title:String = ""
    var content:String = ""
    var reward:String =""
    var whatsapp:String=""
    var instagram:String=""
    lateinit var selectedDate:String
    lateinit var selectedTime:String
    lateinit var expireTime:LocalDateTime
    lateinit var newQuest: Quest
}