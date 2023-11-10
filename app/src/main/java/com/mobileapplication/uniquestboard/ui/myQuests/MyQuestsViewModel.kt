package com.mobileapplication.uniquestboard.ui.myQuests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobileapplication.uniquestboard.ui.common.Quest

class MyQuestsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    val questList = mutableListOf<Quest>()

}