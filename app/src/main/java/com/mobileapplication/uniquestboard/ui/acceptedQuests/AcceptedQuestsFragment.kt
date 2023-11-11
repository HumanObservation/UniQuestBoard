package com.mobileapplication.uniquestboard.ui.acceptedQuests

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
import com.mobileapplication.uniquestboard.databinding.FragmentAcceptedQuestsBinding
import com.mobileapplication.uniquestboard.ui.common.QuestListAdapter
import com.mobileapplication.uniquestboard.ui.common.QuestsContainer
import com.mobileapplication.uniquestboard.ui.myQuests.MyQuestsViewModel
import java.time.LocalDateTime

class AcceptedQuestsFragment : QuestsContainer() {

    private var _binding: FragmentAcceptedQuestsBinding? = null
    private val viewModel: AcceptedQuestsViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
savedInstanceState: Bundle?
): View {
    val acceptedQuestsViewModel =
        ViewModelProvider(this).get(AcceptedQuestsViewModel::class.java)

    _binding = FragmentAcceptedQuestsBinding.inflate(inflater, container, false)
    val root: View = binding.root

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
    val recyclerView: RecyclerView = binding.recyclerView;
    recyclerView.layoutManager = LinearLayoutManager(activity)
    recyclerView.adapter = QuestListAdapter(questList)
    return root
}

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}
}