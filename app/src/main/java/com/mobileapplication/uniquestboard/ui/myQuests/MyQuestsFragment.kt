package com.mobileapplication.uniquestboard.ui.myQuests

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobileapplication.uniquestboard.ui.common.Contact
import com.mobileapplication.uniquestboard.ui.common.Quest
import com.mobileapplication.uniquestboard.ui.common.Status
import com.mobileapplication.uniquestboard.databinding.FragmentMyQuestsBinding
import com.mobileapplication.uniquestboard.ui.common.QuestListAdapter
import com.mobileapplication.uniquestboard.ui.base.QuestsContainer
import com.scwang.smart.refresh.layout.api.RefreshLayout
import java.time.LocalDateTime

class MyQuestsFragment : QuestsContainer() {

    private var _binding: FragmentMyQuestsBinding? = null
    private val viewModel:MyQuestsViewModel by viewModels()

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
        if(viewModel.liveQuestList.value?.isEmpty() == true) addQuestToQuestList()
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
        recyclerView.adapter = viewModel.liveQuestList.value?.let { QuestListAdapter(it) }
        viewModel.liveQuestList.observe(viewLifecycleOwner, Observer { newDataList ->
            // 在这里更新UI或执行其他操作
            recyclerView.adapter = viewModel.liveQuestList.value?.let { QuestListAdapter(it) }
        })
        setHeaderAndFooter()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.liveQuestList.value?.clear()
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
        viewModel.appendQuest(quest1)

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