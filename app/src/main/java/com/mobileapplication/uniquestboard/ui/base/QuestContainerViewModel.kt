package com.mobileapplication.uniquestboard.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobileapplication.uniquestboard.ui.common.Quest

open class QuestContainerViewModel : ViewModel() {
    //var questList = mutableListOf<Quest>()
    private val _questList = MutableLiveData<MutableList<Quest>>()

    // 提供外部类访问LiveData的不可变版本
    val liveQuestList: MutableLiveData<MutableList<Quest>> get() = _questList

    // 公共方法，用于更新Quest列表
    fun updateQuestList(newList: MutableList<Quest>) {
        _questList.value = newList
    }
    fun appendQuestList(appendList: MutableList<Quest>){
        val currentList = _questList.value?.toMutableList() ?: mutableListOf()
        currentList.addAll(appendList)
        _questList.value= currentList
    }
    fun appendQuest(appendQuest:Quest){
        val currentList = _questList.value?.toMutableList() ?: mutableListOf()
        currentList.add(appendQuest)
        _questList.value = currentList
    }
    public var curQuest: Quest? = null;


}