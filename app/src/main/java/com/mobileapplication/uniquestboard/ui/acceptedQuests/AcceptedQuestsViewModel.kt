package com.mobileapplication.uniquestboard.ui.acceptedQuests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobileapplication.uniquestboard.ui.base.QuestContainerViewModel

class AcceptedQuestsViewModel : QuestContainerViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}