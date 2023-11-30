package com.mobileapplication.uniquestboard.ui.myQuests

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobileapplication.uniquestboard.ui.common.Contact
import com.mobileapplication.uniquestboard.ui.common.Quest
import com.mobileapplication.uniquestboard.ui.common.Status
import com.mobileapplication.uniquestboard.databinding.FragmentMyQuestsBinding
import com.mobileapplication.uniquestboard.ui.common.QuestListAdapter
import com.mobileapplication.uniquestboard.ui.base.QuestsContainer
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

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

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyQuestsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.questListInclude.recyclerView;
        recyclerView.layoutManager = LinearLayoutManager(activity)
        viewModel.liveQuestList.observe(viewLifecycleOwner, Observer { newDataList ->
            // 在这里更新UI或执行其他操作
            updateAdapter(false)
        })
        setHeaderAndFooter()
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addQuestToQuestList()
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

    private fun updateAdapter(isLoad:Boolean) : Boolean{
        val recyclerView: RecyclerView = binding.questListInclude.recyclerView;
        if(isLoad &&
            viewModel.liveQuestList.value?.isNotEmpty() == true &&
            viewModel.liveQuestList.value?.size == viewModel.curPosition){
            Toast.makeText(this.context,"No more quests!", Toast.LENGTH_LONG).show()
            return false;
        }
        else if(viewModel.liveQuestList.value?.isNotEmpty() == true &&
            viewModel.liveQuestList.value!!.size > viewModel.numOfQuestPerLoad*(viewModel.currrentPage+1)){
            recyclerView.adapter = viewModel.liveQuestList.value?.take(viewModel.numOfQuestPerLoad*(viewModel.currrentPage+1)).let { QuestListAdapter(it) }
            viewModel.curPosition = viewModel.numOfQuestPerLoad*(viewModel.currrentPage+1)
            return true;
        }
        else if(viewModel.liveQuestList.value?.isNotEmpty() == true
            &&viewModel.numOfQuestPerLoad*(viewModel.currrentPage)<viewModel.liveQuestList.value!!.size
            && viewModel.liveQuestList.value!!.size<= viewModel.numOfQuestPerLoad*(viewModel.currrentPage+1)){
            recyclerView.adapter = viewModel.liveQuestList.value?.let { QuestListAdapter(it) }
            viewModel.curPosition = viewModel.liveQuestList.value?.size!!
            return true;
        }
        recyclerView.adapter = viewModel.liveQuestList.value?.let { QuestListAdapter(it) }
        return false
    }

    private fun LoadMore(){
        if(viewModel.currrentPage*viewModel.numOfQuestPerLoad < viewModel.curPosition){
            viewModel.currrentPage++
        }
        if(updateAdapter(true)) viewModel.currrentPage++
    }
    private fun setHeaderAndFooter(){
        val refreshLayout = binding.questListInclude.refreshLayout as RefreshLayout
        refreshLayout.setOnRefreshListener { refreshlayout ->
            viewModel.liveQuestList.value?.clear()
            viewModel.currrentPage = 0
            viewModel.curPosition = 0
            //TODO:重新获取numOfQuestsPerGet个quest并放入viewModel.questList中
            //成功->
            refreshlayout.finishRefresh(2000 /*,false*/) 
            //失败->
            refreshlayout.finishRefresh(false) //传入false表示刷新失败
        }
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            //TODO:获取更多quest并且append到viewModel.questList中
            //成功->
            refreshlayout.finishLoadMore(1000)
            lifecycleScope.launch(Dispatchers.Main) {
                // 在主线程中执行UI更新
                // 延迟1秒后执行某个函数
                delay(1000)
                // 在主线程中执行某个函数
                LoadMore()
            }
        }
    }
}