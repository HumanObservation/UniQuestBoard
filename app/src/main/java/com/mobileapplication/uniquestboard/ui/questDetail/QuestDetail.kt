package com.mobileapplication.uniquestboard.ui.questDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobileapplication.uniquestboard.R

class QuestDetail : Fragment() {

    companion object {
        fun newInstance() = QuestDetail()
    }

    private lateinit var viewModel: QuestDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quest_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(QuestDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}