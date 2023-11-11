package com.mobileapplication.uniquestboard.ui.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobileapplication.uniquestboard.ui.common.QuestContainerViewModel

class BoardViewModel : QuestContainerViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}