package com.mobileapplication.uniquestboard.ui.board

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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mobileapplication.uniquestboard.GlobalVariables
import com.mobileapplication.uniquestboard.databinding.FragmentBoardBinding
import com.mobileapplication.uniquestboard.ui.base.QuestsContainer
import com.mobileapplication.uniquestboard.ui.base.VolleyCallback
import com.mobileapplication.uniquestboard.ui.common.Contact
import com.mobileapplication.uniquestboard.ui.common.Quest
import com.mobileapplication.uniquestboard.ui.common.QuestListAdapter
import com.mobileapplication.uniquestboard.ui.common.Status
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDateTime

interface VolleyCallback {
    fun onSuccess(response: Quest)
    fun onError(error: String)
}
class BoardFragment : QuestsContainer() {

    private var _binding: FragmentBoardBinding? = null
    private val viewModel: BoardViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override val TAG: String = "BoardFragment"

    private fun getResponse(callback: VolleyCallback)
    {
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
                    var status = Status.values()[js.getString("status").toInt()];
                    val image = mutableListOf<String>()
                    var quest1: Quest = Quest(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
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
                    if(js.getString("publisher") != GlobalVariables.user.itsc && (js.getString("status") == "0"))
                    {
                        callback.onSuccess(quest1)
                    }
                }
                Log.i(TAG, response.toString());
                Toast.makeText(requireActivity().getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show(); },
            Response.ErrorListener { e -> callback.onError(e.toString());Toast.makeText(requireActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show() }) {}
        rq.add(sr);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        getResponse(object : VolleyCallback {
            override fun onSuccess(response: Quest) {
                viewModel.appendQuest(response);
            }

            override fun onError(error: String) {
                Log.e(TAG,"getResponse Error")
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Log.i(TAG,"Enter onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun addTestQuest(){
//        val questList = mutableListOf<Quest>()
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
//            status,
//            image,
//            "thankfulness",
//            contact,
//            "g"
//        )
//
//        viewModel.appendQuest(quest1)
//        taker.add("anyone else")
//        var quest2: Quest = Quest(
//            LocalDateTime.now(),
//            LocalDateTime.now(),
//            "someone else",
//            taker,
//            "the title aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
//            "the content bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
//            Status.PENDING,
//            image,
//            "thankfulness",
//            contact,
//            "g"
//        )
//        viewModel.appendQuest(quest2)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setHeaderAndFooter(){
        val refreshLayout = binding.questListInclude.refreshLayout as RefreshLayout
        refreshLayout.setOnRefreshListener { refreshlayout ->
            viewModel.liveQuestList.value?.clear()
            getResponse(object : VolleyCallback {
                override fun onSuccess(response: Quest) {
                    viewModel.appendQuest(response);
                    //成功->
                    refreshlayout.finishRefresh(2000 /*,false*/) //传入false表示刷新失败
                }

                override fun onError(error: String) {
                    //失败->
                    refreshlayout.finishRefresh(false) //传入false表示刷新失败
                }
            })
            //TODO:重新获取numOfQuestsPerGet个quest并放入viewModel.questList中

        }
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            //TODO:获取更多quest并且append到viewModel.questList中
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

}
