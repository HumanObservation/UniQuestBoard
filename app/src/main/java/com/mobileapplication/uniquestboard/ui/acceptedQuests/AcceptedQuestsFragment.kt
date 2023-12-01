package com.mobileapplication.uniquestboard.ui.acceptedQuests

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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mobileapplication.uniquestboard.GlobalVariables
import com.mobileapplication.uniquestboard.databinding.FragmentAcceptedQuestsBinding
import com.mobileapplication.uniquestboard.ui.base.ContainerAction
import com.mobileapplication.uniquestboard.ui.common.QuestListAdapter
import com.mobileapplication.uniquestboard.ui.base.QuestsContainer
import com.mobileapplication.uniquestboard.ui.base.VolleyCallback
import com.mobileapplication.uniquestboard.ui.common.Contact
import com.mobileapplication.uniquestboard.ui.common.Quest
import com.mobileapplication.uniquestboard.ui.common.Status
import com.scwang.smart.refresh.layout.api.RefreshLayout
import org.json.JSONObject
import java.time.LocalDateTime

interface VolleyCallback {
    fun onSuccess(response: Quest)
    fun onError(error: String)
}
class AcceptedQuestsFragment : QuestsContainer() {

    override val TAG: String = "AcceptedQuestsFragment"
    override var action: ContainerAction = ContainerAction.GetByTaker
    private var _binding: FragmentAcceptedQuestsBinding? = null
    private val viewModel: AcceptedQuestsViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private fun getResponse(callback: VolleyCallback)
    {
        var rq = Volley.newRequestQueue(requireActivity().getApplicationContext());
        var url : String = "http://${GlobalVariables.ip}:${GlobalVariables.port}/android/DB_AcceptedQuest.php";
        var sr = @RequiresApi(Build.VERSION_CODES.O)
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                if(response.toString().trim() != "The record is not found.")
                {
                    Log.i("rp", response.toString());
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
                            enumValues<Status>().firstOrNull { it.ordinal == js.getString("status").toInt() }!!,
                            image,
                            js.getString("reward"),
                            contact,
                            js.getString("order_id")
                        )
                        callback.onSuccess(quest1)
                    }

                    Toast.makeText(requireActivity().getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();} },
            Response.ErrorListener { e -> callback.onError(e.toString()); callback.onError(e.toString());Toast.makeText(requireActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show() })
        {
            override fun getParams(): MutableMap<String, String>? {
                var params = HashMap<String, String>();
                params.put("itsc", GlobalVariables.user.itsc);
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.liveQuestList.value?.clear()
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