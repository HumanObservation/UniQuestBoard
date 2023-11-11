package com.mobileapplication.uniquestboard.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobileapplication.uniquestboard.ui.common.Quest

open class QuestContainerViewModel : ViewModel() {
    val questList = mutableListOf<Quest>()

}