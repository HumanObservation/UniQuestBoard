package com.mobileapplication.uniquestboard

import android.widget.Button
import android.widget.EditText
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.mobileapplication.uniquestboard.ui.login.LoginActivity
import com.mobileapplication.uniquestboard.ui.register.RegisterActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterTest {
    @get:Rule
    val activityRule: ActivityScenarioRule<RegisterActivity> = ActivityScenarioRule(RegisterActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testRegister()
    {
        val scenario: ActivityScenario<RegisterActivity> = activityRule.scenario
        scenario.onActivity { ay ->
            val et1 = ay.findViewById<EditText>(R.id.itsc)
            val et2 = ay.findViewById<EditText>(R.id.password)
            val et3 = ay.findViewById<EditText>(R.id.email)
            val et4 = ay.findViewById<EditText>(R.id.student_id)
            val rbtn = ay.findViewById<Button>(R.id.register_1)
            ay.runOnUiThread {
                et1.setText("kamaw")
                et2.setText("oop123")
                et3.setText("kamaw@connect.ust.hk")
                et4.setText("20796571")
            }
            rbtn.performClick()
        }
        Thread.sleep(3000);
        Intents.intended(IntentMatchers.hasComponent(LoginActivity::class.java.name))
    }
}