package com.mobileapplication.uniquestboard.ui.questDetail

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.mobileapplication.uniquestboard.databinding.FragmentQuestDetailBinding
import com.mobileapplication.uniquestboard.databinding.QuestDetailBinding
import com.mobileapplication.uniquestboard.ui.common.QuestsContainer
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

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setQuestDetail(){
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}