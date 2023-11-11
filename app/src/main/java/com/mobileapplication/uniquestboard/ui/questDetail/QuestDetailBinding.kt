package com.mobileapplication.uniquestboard.ui.questDetail
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.mobileapplication.uniquestboard.R

class QuestDetailBinding private constructor(private val rootView: View) : ViewBinding {

    // Add your view references here
    // Example: val textView: TextView = rootView.findViewById(R.id.textView)

    companion object {
        fun inflate(inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): QuestDetailBinding {
            val rootView = inflater.inflate(R.layout.quest_detail, parent, attachToParent)
            return QuestDetailBinding(rootView)
        }
    }

    override fun getRoot(): View {
        return rootView
    }

}





