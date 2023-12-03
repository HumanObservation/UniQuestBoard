package com.mobileapplication.uniquestboard


import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.mobileapplication.uniquestboard.ui.login.LoginActivity
import com.mobileapplication.uniquestboard.ui.questDetail.QuestDetailFragment
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import org.junit.After
import org.junit.Before
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class LoginTest {

    @get:Rule
    val activityRule: ActivityScenarioRule<LoginActivity> = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testlogin()
    {
        val scenario: ActivityScenario<LoginActivity> = activityRule.scenario
        scenario.onActivity { ay ->
            val et1 = ay.findViewById<EditText>(R.id.itsc)
            val et2 = ay.findViewById<EditText>(R.id.password)
            val lbtn = ay.findViewById<Button>(R.id.login)
            ay.runOnUiThread {
                et1.setText("ivanaw")
                et2.setText("123456")
            }
            lbtn.performClick()
        }
        Thread.sleep(3000);
        intended(hasComponent(MainActivity::class.java.name))
    }
}