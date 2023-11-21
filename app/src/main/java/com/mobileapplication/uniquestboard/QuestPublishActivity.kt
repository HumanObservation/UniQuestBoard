package com.mobileapplication.uniquestboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.mobileapplication.uniquestboard.R
import com.mobileapplication.uniquestboard.databinding.ActivityQuestDetailBinding
import com.mobileapplication.uniquestboard.databinding.ActivityQuestPublishBinding
import com.mobileapplication.uniquestboard.ui.questDetail.QuestDetailFragment
import com.mobileapplication.uniquestboard.ui.questPublish.QuestPublishFragment

class QuestPublishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestPublishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_publish)
        binding = ActivityQuestPublishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val fragment = QuestPublishFragment()

            // Use FragmentTransaction to add the fragment to the container
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
        setUpToolBar()
    }

    fun setUpToolBar(){
        setSupportActionBar(binding.appBarQuestPublish.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        binding.appBarQuestPublish.toolbar?.title= "Quest Publish"
    }
    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}