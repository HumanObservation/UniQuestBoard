package com.mobileapplication.uniquestboard.ui.myQuests

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobileapplication.uniquestboard.ui.common.Contact
import com.mobileapplication.uniquestboard.ui.common.Quest
import com.mobileapplication.uniquestboard.ui.common.Status
import com.mobileapplication.uniquestboard.databinding.FragmentMyQuestsBinding
import com.mobileapplication.uniquestboard.ui.common.QuestListAdapter
import com.mobileapplication.uniquestboard.ui.base.QuestsContainer
import java.time.LocalDateTime

class MyQuestsFragment : QuestsContainer() {

    private var _binding: FragmentMyQuestsBinding? = null
    private val myQuestsViewModel:MyQuestsViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAttach(context: Context) {
        super.onAttach(context)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(myQuestsViewModel.liveQuestList.value?.isEmpty() == true) addQuestToQuestList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyQuestsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.questListInclude.recyclerView;
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = myQuestsViewModel.liveQuestList.value?.let { QuestListAdapter(it) }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        myQuestsViewModel.liveQuestList.value?.clear()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addQuestToQuestList(){
        val taker = mutableListOf<String>()
        taker.add("someone")
        var contact = Contact("55556666","@some_one")
        var status = Status.COMPLETED;
        val image = mutableListOf<String>()
        var quest1: Quest = Quest(
            LocalDateTime.now(),
            LocalDateTime.now(),
            "admin",
            taker,
            "the title",
            "the content",
            Status.FAILED,
            image,
            "thankfulness",
            contact
        )
        myQuestsViewModel.appendQuest(quest1)

    }
}