package com.mobileapplication.uniquestboard.ui.common

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mobileapplication.uniquestboard.QuestDetailActivity
import com.mobileapplication.uniquestboard.R
import java.time.format.DateTimeFormatter

public interface OnItemClickListener{
    fun onItemCllick(position:Int)
}

class QuestListAdapter(private val questList: List<Quest>?) :
    RecyclerView.Adapter<QuestListAdapter.ViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.O)
    var df = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
    var TAG:String = "QuestAdapter"
    private lateinit var context:Context;// = context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val expireTimeTextView:TextView = itemView.findViewById(R.id.expireTimeTextView)
        val questCard:CardView = itemView.findViewById(R.id.questCard)
        init{
            itemView.setOnClickListener{
                val position = adapterPosition
                if(position!=RecyclerView.NO_POSITION){
                    val quest  = questList?.get(position)
                    val intent = Intent(itemView.context,QuestDetailActivity::class.java)
                    if (quest != null) {
                        intent.putExtra("questID",quest.questID)
                    }
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.quest, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quest = questList?.get(position)
        if (quest != null) {
            holder.titleTextView.text = quest.title
        }
        if (quest != null) {
            holder.contentTextView.text = quest.content
        }
        holder.expireTimeTextView.text = "Expire : " + df.format(quest!!.expiredTime);
        val backgroundColor:Int = quest!!.let { getCardColor(it.status) }
        holder.questCard.setCardBackgroundColor(ContextCompat.getColor(context, backgroundColor))
    }

    override fun getItemCount(): Int {
        return questList!!.size
    }
}