package com.mobileapplication.uniquestboard.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobileapplication.uniquestboard.ui.common.Quest

open class QuestContainerViewModel : ViewModel() {
    var questList = mutableListOf<Quest>()
    public lateinit var curQuest: Quest

}