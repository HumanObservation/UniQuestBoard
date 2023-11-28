package com.mobileapplication.uniquestboard.ui.acceptedQuests

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobileapplication.uniquestboard.databinding.FragmentAcceptedQuestsBinding
import com.mobileapplication.uniquestboard.ui.base.ContainerAction
import com.mobileapplication.uniquestboard.ui.common.QuestListAdapter
import com.mobileapplication.uniquestboard.ui.base.QuestsContainer
import com.scwang.smart.refresh.layout.api.RefreshLayout
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
    viewModel.liveQuestList.observe(viewLifecycleOwner, Observer { newDataList ->
        // 在这里更新UI或执行其他操作
        recyclerView.adapter = viewModel.liveQuestList.value?.let { QuestListAdapter(it) }
    })
    setHeaderAndFooter()
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
    private fun setHeaderAndFooter(){
        val refreshLayout = binding.questListInclude.refreshLayout as RefreshLayout
        refreshLayout.setOnRefreshListener { refreshlayout ->
            viewModel.liveQuestList.value?.clear()
            //TODO:重新获取numOfQuestsPerGet个quest并放入viewModel.questList中
            //成功->
            refreshlayout.finishRefresh(2000 /*,false*/) //传入false表示刷新失败
            //失败->
            refreshlayout.finishRefresh(false) //传入false表示刷新失败
        }
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            //TODO:获取更多quest并且append到viewModel.questList中
            //成功->
            refreshlayout.finishLoadMore(2000 ) //传入false表示加载失败
            //失败->
            refreshlayout.finishLoadMore(false )
        }
    }
}