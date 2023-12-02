package com.mobileapplication.uniquestboard.ui.questDetail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mobileapplication.uniquestboard.GlobalVariables
import com.mobileapplication.uniquestboard.MainActivity
import com.mobileapplication.uniquestboard.User
import com.mobileapplication.uniquestboard.databinding.FragmentQuestDetailBinding
import com.mobileapplication.uniquestboard.ui.base.QuestsContainer
import com.mobileapplication.uniquestboard.ui.base.VolleyCallback
import com.mobileapplication.uniquestboard.ui.common.Quest
import com.mobileapplication.uniquestboard.ui.common.Status
import com.mobileapplication.uniquestboard.ui.common.getCardColor
import java.util.UUID

interface VolleyCallback {
    fun onSuccess(response: Quest)
    fun onError(error: String)
}
class QuestDetailFragment : QuestsContainer() {

    companion object {
        fun newInstance() = QuestDetailFragment()
    }
    override val TAG:String = "QuestDetailFragment"
    private var _binding: FragmentQuestDetailBinding? = null
    private val binding get() = _binding!!
    val viewModel: QuestDetailViewModel by viewModels()
    private val activity = getActivity()
    private val context = getContext()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //setQuestDetail()
        return root //inflater.inflate(R.layout.fragment_quest_detail, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: String? = arguments?.getString("questID")
        getAQuest(data, object : VolleyCallback {
            override fun onSuccess(response: Quest) {
                viewModel.curQuest = response;
                setQuestDetail()
                setUpContactInformation()
                setUpStatusChangingButton()
            }

            override fun onError(error: String) {
                //TODO:("Not yet implemented")
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setQuestDetail(){
        //TODO:根据questID获取quest
        val quest = viewModel.curQuest
        Log.d(TAG,"Set Title to " + quest!!.title)
        val component = binding.includeQuestDetail
        component.tvTitle.text = quest!!.title
        component.tvContent.text = quest!!.content
        component.tvExpireTime.text = "Expire : " + df.format(quest!!.expiredTime)
        component.tvPublishTime.text = "Published : "+df.format(quest!!.publishTime)
        component.tvPublisher.text = "Published by " + quest!!.publisher
        component.tvReward.text = "Reward : "+ quest.reward
        component.questDetailCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), getCardColor(quest!!.status)))
//        HiddenInfo
//
//        coverImage

    }

    private fun setUpContactInformation(){
        //设定整个ContactInformation是否应该显示
        if(viewModel.curQuest!!.taker != null &&
            viewModel.curQuest!!.taker == GlobalVariables.user.itsc ||
            viewModel.curQuest!!.publisher == GlobalVariables.user.itsc){
            binding.includeQuestDetail.contactInformationContainer.visibility = View.VISIBLE
        }
        else{
            binding.includeQuestDetail.contactInformationContainer.visibility = View.GONE
        }

        //设定单个Contact是否显示，根据内容是否为空
        var instagramUserName = binding.includeQuestDetail.InstagramUserName
        var whatsappNumber = binding.includeQuestDetail.whatsappNum
        if(viewModel.curQuest!!.contact.instagram.isNullOrBlank()){
            binding.includeQuestDetail.instagramContectInformation.visibility = View.GONE
        }
        else{
            instagramUserName.text =viewModel.curQuest!!.contact.instagram
        }
        if(viewModel.curQuest!!.contact.whatsapp.isNullOrBlank()){
            binding.includeQuestDetail.whatsappContectInformation.visibility = View.GONE
        }
        else{
            whatsappNumber.text = viewModel.curQuest!!.contact.whatsapp
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpStatusChangingButton(){
        //可视与否设定
        val takeButton = binding.includeQuestDetail.takeButton
        val cancelButton = binding.includeQuestDetail.cancelButton
        val completeButton = binding.includeQuestDetail.completeButton
        if(viewModel.curQuest!!.status == Status.COMPLETED ||
            viewModel.curQuest!!.status == Status.INTERRUPTED||
            viewModel.curQuest!!.status == Status.FAILED ||
            viewModel.curQuest!!.status == Status.EXPIRED){
            takeButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
            completeButton.visibility = View.GONE

        }
        else if(viewModel.curQuest!!.publisher == GlobalVariables.user.itsc){
            takeButton.visibility = View.GONE
            cancelButton.visibility = View.VISIBLE
            completeButton.visibility = View.GONE
        }
        else if(viewModel.curQuest!!.taker != null && viewModel.curQuest!!.taker == GlobalVariables.user.itsc){
            cancelButton.visibility = View.GONE
            takeButton.visibility = View.GONE
            completeButton.visibility = View.VISIBLE
        }
        else{
            Log.i("g", viewModel.curQuest!!.taker.toString());
            takeButton.visibility = View.VISIBLE
            cancelButton.visibility = View.GONE
            completeButton.visibility = View.GONE
        }
        //按钮逻辑设定
        takeButton.setOnClickListener(){
            viewModel.curQuest!!.status = Status.IN_PROGRESS
            viewModel.curQuest!!.taker = GlobalVariables.user.itsc
            //TODO:将更改同步到db
            val id: String? = arguments?.getString("questID")
            var url : String = "http://${GlobalVariables.ip}:${GlobalVariables.port}/android/DB_AcceptQuest.php";
            var rq = Volley.newRequestQueue(requireActivity().applicationContext);
            var sr = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response ->
                    Toast.makeText(requireActivity().applicationContext, "Login successes", Toast.LENGTH_SHORT).show(); },
                Response.ErrorListener { e -> Toast.makeText(requireActivity().applicationContext,
                    e.toString(), Toast.LENGTH_SHORT).show() })
            {
                override fun getParams(): MutableMap<String, String>? {
                    var params = HashMap<String, String>();
                    params.put("itsc", GlobalVariables.user.itsc);
                    params.put("order_id", id!!);
                    return params;
                }
            }
            rq.add(sr);
            RefreshUI()
        }

        cancelButton.setOnClickListener(){
            viewModel.curQuest!!.status = Status.INTERRUPTED
            //TODO:将更改同步到db
            val id: String? = arguments?.getString("questID")
            var url : String = "http://${GlobalVariables.ip}:${GlobalVariables.port}/android/DB_CancelQuest.php";
            var rq = Volley.newRequestQueue(requireActivity().applicationContext);
            var sr = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response ->
                    Toast.makeText(requireActivity().applicationContext, "Login successes", Toast.LENGTH_SHORT).show(); },
                Response.ErrorListener { e -> Toast.makeText(requireActivity().applicationContext,
                    e.toString(), Toast.LENGTH_SHORT).show() })
            {
                override fun getParams(): MutableMap<String, String>? {
                    var params = HashMap<String, String>();
                    params.put("order_id", id!!);
                    return params;
                }
            }
            rq.add(sr);
            RefreshUI()
        }

        completeButton.setOnClickListener(){
            viewModel.curQuest!!.status = Status.COMPLETED
            //TODO:将更改同步到db
            val id: String? = arguments?.getString("questID")
            var url : String = "http://${GlobalVariables.ip}:${GlobalVariables.port}/android/DB_CompleteQuest.php";
            var rq = Volley.newRequestQueue(requireActivity().applicationContext);
            var sr = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response ->
                    Toast.makeText(requireActivity().applicationContext, "Login successes", Toast.LENGTH_SHORT).show(); },
                Response.ErrorListener { e -> Toast.makeText(requireActivity().applicationContext,
                    e.toString(), Toast.LENGTH_SHORT).show() })
            {
                override fun getParams(): MutableMap<String, String>? {
                    var params = HashMap<String, String>();
                    params.put("order_id", id!!);
                    return params;
                }
            }
            rq.add(sr);
            RefreshUI()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun RefreshUI(){
        setQuestDetail()
        setUpContactInformation()
        setUpStatusChangingButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}