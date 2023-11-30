package com.mobileapplication.uniquestboard.ui.questDetail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.mobileapplication.uniquestboard.GlobalVariables
import com.mobileapplication.uniquestboard.databinding.FragmentQuestDetailBinding
import com.mobileapplication.uniquestboard.ui.base.QuestsContainer
import com.mobileapplication.uniquestboard.ui.common.Status
import com.mobileapplication.uniquestboard.ui.common.getCardColor
import java.util.UUID

class QuestDetailFragment : QuestsContainer() {

    companion object {
        fun newInstance() = QuestDetailFragment()
    }
    override val TAG:String = "QuestDetailFragment"
    private var _binding: FragmentQuestDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuestDetailViewModel by viewModels()
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
        viewModel.curQuest = getAQuest(UUID.randomUUID())
        setQuestDetail()
        setUpContactInformation()
        setUpStatusChangingButton()

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
        Log.d(TAG,"Set Title to " + quest.title)
        val component = binding.includeQuestDetail
        component.tvTitle.text = quest.title
        component.tvContent.text = quest.content
        component.tvExpireTime.text = "Expire : " + df.format(quest.expiredTime)
        component.tvPublishTime.text = "Published : "+df.format(quest.publishTime)
        component.tvPublisher.text = "Published by " + quest.publisher
        component.questDetailCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), getCardColor(quest.status)))
        //HiddenInfo

        //coverImage

    }

    private fun setUpContactInformation(){
        //设定整个ContactInformation是否应该显示
        if(viewModel.curQuest.taker.contains(GlobalVariables.user.itsc)){
            binding.includeQuestDetail.contactInformationContainer.visibility = View.VISIBLE
        }
        else{
            binding.includeQuestDetail.contactInformationContainer.visibility = View.GONE
        }

        //设定单个Contact是否显示，根据内容是否为空
        var instagramUserName = binding.includeQuestDetail.InstagramUserName
        var whatsappNumber = binding.includeQuestDetail.whatsappNum
        if(viewModel.curQuest.contact.instagram.isNullOrBlank()){
            binding.includeQuestDetail.instagramContectInformation.visibility = View.GONE
        }
        else{
            instagramUserName.text =viewModel.curQuest.contact.instagram
        }
        if(viewModel.curQuest.contact.whatsapp.isNullOrBlank()){
            binding.includeQuestDetail.whatsappContectInformation.visibility = View.GONE
        }
        else{
            whatsappNumber.text = viewModel.curQuest.contact.whatsapp
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpStatusChangingButton(){
        //可视与否设定
        val takeButton = binding.includeQuestDetail.takeButton
        val cancelButton = binding.includeQuestDetail.cancelButton
        val completeButton = binding.includeQuestDetail.completeButton
        if(viewModel.curQuest.status == Status.COMPLETED ||
            viewModel.curQuest.status == Status.INTERRUPTED||
            viewModel.curQuest.status == Status.FAILED ||
            viewModel.curQuest.status == Status.EXPIRED){
            takeButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
            completeButton.visibility = View.GONE

        }
        else if(viewModel.curQuest.publisher == GlobalVariables.user.itsc){
            takeButton.visibility = View.GONE
            cancelButton.visibility = View.VISIBLE
            completeButton.visibility = View.GONE
        }
        else if(viewModel.curQuest.taker.contains(GlobalVariables.user.itsc)){
            cancelButton.visibility = View.GONE
            takeButton.visibility = View.GONE
            completeButton.visibility = View.VISIBLE
        }
        else{
            takeButton.visibility = View.VISIBLE
            cancelButton.visibility = View.GONE
            completeButton.visibility = View.GONE
        }
        //按钮逻辑设定
        takeButton.setOnClickListener(){
            viewModel.curQuest.status = Status.IN_PROGRESS
            viewModel.curQuest.taker.add(GlobalVariables.user.itsc)
            //TODO:将更改同步到db
            RefreshUI()
        }

        cancelButton.setOnClickListener(){
            viewModel.curQuest.status = Status.INTERRUPTED
            //TODO:将更改同步到db
            RefreshUI()
        }

        completeButton.setOnClickListener(){
            viewModel.curQuest.status = Status.COMPLETED
            //TODO:将更改同步到db
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