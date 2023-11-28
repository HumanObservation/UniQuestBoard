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
import com.mobileapplication.uniquestboard.databinding.FragmentAcceptedQuestsBinding
import com.mobileapplication.uniquestboard.ui.base.ContainerAction
import com.mobileapplication.uniquestboard.ui.common.QuestListAdapter
import com.mobileapplication.uniquestboard.ui.base.QuestsContainer
import java.util.UUID

class AcceptedQuestsFragment : QuestsContainer() {

    override val TAG: String = "AcceptedQuestsFragment"
    override var action: ContainerAction = ContainerAction.GetByTaker
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
    val recyclerView: RecyclerView = binding.questListInclude.recyclerView;
    recyclerView.layoutManager = LinearLayoutManager(activity)
    recyclerView.adapter = viewModel.liveQuestList.value?.let { QuestListAdapter(it) }
    return root
}

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.appendQuest(getAQuest(UUID.randomUUID()))
    }

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}
}