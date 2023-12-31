package com.mobileapplication.uniquestboard.ui.myQuests

import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mobileapplication.uniquestboard.GlobalVariables
import com.mobileapplication.uniquestboard.ui.common.Contact
import com.mobileapplication.uniquestboard.ui.common.Quest
import com.mobileapplication.uniquestboard.ui.common.Status
import com.mobileapplication.uniquestboard.databinding.FragmentMyQuestsBinding
import com.mobileapplication.uniquestboard.ui.common.QuestListAdapter
import com.mobileapplication.uniquestboard.ui.base.QuestsContainer
import com.mobileapplication.uniquestboard.ui.base.VolleyCallback
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDateTime
import java.util.UUID

interface VolleyCallback {
    fun onSuccess(response: Quest)
    fun onError(error: String)
}
class MyQuestsFragment : QuestsContainer() {

    private var _binding: FragmentMyQuestsBinding? = null
    private val viewModel : MyQuestsViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun getResponse(callback: VolleyCallback)
    {
        var rq = Volley.newRequestQueue(requireActivity().getApplicationContext());
        var url : String = "http://${GlobalVariables.ip}:${GlobalVariables.port}/android/DB_MyQuest.php";
        var sr = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                if(response.toString().trim() != "The record is not found.")
                {
                    val jsonObject: JSONObject = JSONObject(response.toString())
                    for(i in 1..jsonObject.length())
                    {
                        Log.i(i.toString(), jsonObject.getString(i.toString()))
                        val result = jsonObject.getString(i.toString()).toString()
                        val sub = result.substring(1, result.length - 1);
                        val js: JSONObject = JSONObject(sub)
                        Log.i(i.toString(), js.getString("title"));
                        var ct = js.getString("contact");
                        var ctsub = ct.substring(0, 2);
                        var contact : Contact;
                        if(ctsub == "IG")
                        {
                            contact = Contact(null, js.getString("contact"))
                        }
                        else
                        {
                            contact = Contact(js.getString("contact"), null)
                        }
                        var status = Status.COMPLETED;
                        val image = mutableListOf<String>()
                        var quest1: Quest = Quest(
                            LocalDateTime.parse(js.getString("publish_date"),df),
                            LocalDateTime.parse(js.getString("expired_date"),df),
                            js.getString("publisher"),
                            null,
                            js.getString("title"),
                            js.getString("description"),
                            enumValues<Status>().firstOrNull { it.ordinal == js.getString("status").toInt() }!!,
                            image,
                            js.getString("reward"),
                            contact,
                            js.getString("order_id")
                        )
                        callback.onSuccess(quest1)
                    }

                    Log.i("12", response.toString());
                    Toast.makeText(requireActivity().getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();} },
            Response.ErrorListener { e -> callback.onError(e.toString()); Toast.makeText(requireActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show() })
        {
            override fun getParams(): MutableMap<String, String>? {
                var params = HashMap<String, String>();
                params.put("publisher", GlobalVariables.user.itsc);
                return params;
            }
        }
        rq.add(sr);
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        getResponse(object : VolleyCallback {
            override fun onSuccess(response: Quest) {
                viewModel.appendQuest(response);
            }

            override fun onError(error: String) {
            }
        })
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.liveQuestList.value?.clear()
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun addQuestToQuestList(){
//        val taker = mutableListOf<String>()
//        taker.add("someone")
//        var contact = Contact("55556666","@some_one")
//        var status = Status.COMPLETED;
//        val image = mutableListOf<String>()
//        var quest1: Quest = Quest(
//            LocalDateTime.now(),
//            LocalDateTime.now(),
//            "admin",
//            taker,
//            "the title",
//            "the content",
//            Status.FAILED,
//            image,
//            "thankfulness",
//            contact,
//            "h"
//        )
//        viewModel.appendQuest(quest1)
//
//    }

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
            getResponse(object : VolleyCallback {
                override fun onSuccess(response: Quest) {
                    viewModel.appendQuest(response);
                    //成功->
                    refreshlayout.finishRefresh(500) //传入false表示刷新失败
                }

                override fun onError(error: String) {
                    //失败->
                    refreshlayout.finishRefresh(false) //传入false表示刷新失败
                }
            })
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