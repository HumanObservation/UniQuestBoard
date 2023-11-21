package com.mobileapplication.uniquestboard.ui.myQuests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobileapplication.uniquestboard.ui.base.QuestContainerViewModel

class MyQuestsViewModel : QuestContainerViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

}