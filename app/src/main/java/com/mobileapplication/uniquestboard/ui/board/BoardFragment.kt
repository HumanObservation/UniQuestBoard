package com.mobileapplication.uniquestboard.ui.board

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobileapplication.uniquestboard.ui.common.Contact
import com.mobileapplication.uniquestboard.ui.common.Quest
import com.mobileapplication.uniquestboard.ui.common.Status
import com.mobileapplication.uniquestboard.databinding.FragmentBoardBinding
import com.mobileapplication.uniquestboard.ui.acceptedQuests.AcceptedQuestsViewModel
import com.mobileapplication.uniquestboard.ui.common.QuestListAdapter
import com.mobileapplication.uniquestboard.ui.common.QuestsContainer
import java.time.LocalDateTime

class BoardFragment : QuestsContainer() {

    private var _binding: FragmentBoardBinding? = null
    private val viewModel: BoardViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(BoardViewModel::class.java)

        _binding = FragmentBoardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if(viewModel.questList.isEmpty()) addTestQuest()
        val recyclerView: RecyclerView = binding.recyclerView;
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = QuestListAdapter(viewModel.questList)
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTestQuest(){
        val questList = mutableListOf<Quest>()
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
            status,
            image,
            "thankfulness",
            contact
        )

        viewModel.questList.add(quest1)
        taker.add("anyone else")
        var quest2: Quest = Quest(
            LocalDateTime.now(),
            LocalDateTime.now(),
            "someone else",
            taker,
            "the title aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
            "the content bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
            Status.PENDING,
            image,
            "thankfulness",
            contact
        )
        viewModel.questList.add(quest2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}