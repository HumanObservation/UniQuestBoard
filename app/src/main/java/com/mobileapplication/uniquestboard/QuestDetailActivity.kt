package com.mobileapplication.uniquestboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.mobileapplication.uniquestboard.databinding.ActivityQuestDetailBinding
import com.mobileapplication.uniquestboard.ui.questDetail.QuestDetailFragment

class QuestDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            val fragment = QuestDetailFragment()

            // Use FragmentTransaction to add the fragment to the container
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
        setUpToolBar()
    }

    fun setUpFragment(){

    }

    fun setUpToolBar(){
        setSupportActionBar(binding.appBarQuestDetail.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        binding.appBarQuestDetail.toolbar?.title= "Quest"
    }

    @Override
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