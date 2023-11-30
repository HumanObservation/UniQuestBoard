package com.mobileapplication.uniquestboard.ui.board

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.updateTransition
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mobileapplication.uniquestboard.GlobalVariables
import com.mobileapplication.uniquestboard.databinding.FragmentBoardBinding
import com.mobileapplication.uniquestboard.ui.base.QuestsContainer
import com.mobileapplication.uniquestboard.ui.common.Contact
import com.mobileapplication.uniquestboard.ui.common.Quest
import com.mobileapplication.uniquestboard.ui.common.QuestListAdapter
import com.mobileapplication.uniquestboard.ui.common.Status
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
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

        val recyclerView: RecyclerView = binding.questListInclude.recyclerView;
        recyclerView.layoutManager = LinearLayoutManager(activity)
        viewModel.liveQuestList.observe(viewLifecycleOwner, Observer { newDataList ->
            // 在这里更新UI或执行其他操作
            updateAdapter(false)
        })
        setHeaderAndFooter()
        return root
    }

    override fun onResume() {
        var rq = Volley.newRequestQueue(requireActivity().getApplicationContext());
        var url : String = "http://${GlobalVariables.ip}:${GlobalVariables.port}/android/DB_showAllQuests.php";
        var sr = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val jsonObject: JSONObject = JSONObject(response.toString())
                for(i in 1..jsonObject.length())
                {
                    Log.i(i.toString(), jsonObject.getString(i.toString()))
                    val result = jsonObject.getString(i.toString()).toString()
                    val sub = result.substring(1, result.length - 1);
                    val js: JSONObject = JSONObject(sub)
                    Log.i(i.toString(), js.getString("title"));
                    val questList = mutableListOf<Quest>()
                    val taker = mutableListOf<String>()
                    taker.add("someone")
                    var contact = Contact("55556666","@some_one")
                    var status = Status.COMPLETED;
                    val image = mutableListOf<String>()
                    var quest1: Quest = Quest(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        js.getString("publisher"),
                        taker,
                        js.getString("title"),
                        js.getString("description"),
                        status,
                        image,
                        js.getString("reward"),
                        contact
                    )
                    viewModel.appendQuest(quest1)
                }
                Log.i(TAG, response.toString());
                Toast.makeText(requireActivity().getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show(); },
            Response.ErrorListener { e -> Toast.makeText(requireActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show() }) {}
        rq.add(sr);
        super.onResume()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateAdapter(isLoad:Boolean) : Boolean{
        val recyclerView: RecyclerView = binding.questListInclude.recyclerView;
        if(isLoad &&
            viewModel.liveQuestList.value?.isNotEmpty() == true &&
            viewModel.liveQuestList.value?.size == viewModel.curPosition){
            Toast.makeText(this.context,"No more quests!",Toast.LENGTH_LONG).show()
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
            viewModel.currrentPage = 0
            viewModel.curPosition = 0
            viewModel.liveQuestList.value?.clear()
            updateAdapter(false)

            //TODO:重新获取numOfQuestsPerGet个quest并放入viewModel.questList中
            //成功->
            refreshlayout.finishRefresh(2000 /*,false*/) //传入false表示刷新失败
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
