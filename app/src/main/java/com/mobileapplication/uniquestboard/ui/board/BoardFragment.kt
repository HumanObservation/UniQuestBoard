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
                        contact,
                        js.getString("order_id")
                    )
                    if(js.getString("publisher") != GlobalVariables.user.itsc && js.getString("status") == "0")
                    {
                        callback.onSuccess(quest1)
                    }
                }
                Log.i(TAG, response.toString());
                Toast.makeText(requireActivity().getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show(); },
            Response.ErrorListener { e -> callback.onError(e.toString());Toast.makeText(requireActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show() }) {}
        rq.add(sr);
    }

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
        val homeViewModel =
            ViewModelProvider(this).get(BoardViewModel::class.java)

        _binding = FragmentBoardBinding.inflate(inflater, container, false)
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
            contact,
            "g"
        )

        viewModel.appendQuest(quest1)
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
            contact,
            "g"
        )
        viewModel.appendQuest(quest2)
    }

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
            //成功->
            refreshlayout.finishLoadMore(2000 ) //传入false表示加载失败
            //失败->
            refreshlayout.finishLoadMore(false )
        }
    }

}
