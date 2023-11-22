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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.mobileapplication.uniquestboard.databinding.FragmentBoardBinding
import com.mobileapplication.uniquestboard.ui.common.Contact
import com.mobileapplication.uniquestboard.ui.common.Quest
import com.mobileapplication.uniquestboard.ui.common.Status
import com.mobileapplication.uniquestboard.databinding.FragmentBoardBinding
import com.mobileapplication.uniquestboard.ui.common.QuestListAdapter
import com.mobileapplication.uniquestboard.ui.base.QuestsContainer
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

        val recyclerView: RecyclerView = binding.recyclerView;
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = QuestListAdapter(viewModel.questList)
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
            contact
        )

        viewModel.questList.add(quest1)
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
            contact
        )
        viewModel.questList.add(quest2)
    }

    override fun onResume() {
        var rq = Volley.newRequestQueue(requireActivity().getApplicationContext());
        var url : String = "http://192.168.1.115/android/DB_showAllQuests.php";
        var sr = object : JsonObjectRequest(
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

                    viewModel.questList.add(quest1)
                }

                Log.i("1", response.toString());
                Toast.makeText(requireActivity().getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show(); },
            Response.ErrorListener { e -> Toast.makeText(requireActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show() }) {}
        rq.add(sr);
        super.onResume()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
