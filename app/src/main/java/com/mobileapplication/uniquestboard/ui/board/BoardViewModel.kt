package com.mobileapplication.uniquestboard.ui.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobileapplication.uniquestboard.ui.base.QuestContainerViewModel

class BoardViewModel : QuestContainerViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}