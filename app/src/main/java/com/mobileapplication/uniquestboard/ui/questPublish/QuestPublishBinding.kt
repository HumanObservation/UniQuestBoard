package com.mobileapplication.uniquestboard.ui.questPublish
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.mobileapplication.uniquestboard.R

class QuestPublishBinding private constructor(private val rootView: View) : ViewBinding {

    // Add your view references here
    // Example: val textView: TextView = rootView.findViewById(R.id.textView)

    companion object {
        fun inflate(inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): QuestPublishBinding {
            val rootView = inflater.inflate(R.layout.quest_publish_form, parent, attachToParent)
            return QuestPublishBinding(rootView)
        }
    }

    override fun getRoot(): View {
        return rootView
    }

}





