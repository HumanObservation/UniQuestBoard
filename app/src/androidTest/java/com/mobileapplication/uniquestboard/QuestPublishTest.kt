package com.mobileapplication.uniquestboard
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import androidx.test.runner.AndroidJUnit4
import com.mobileapplication.uniquestboard.ui.questPublish.QuestPublishBinding
import com.mobileapplication.uniquestboard.ui.questPublish.QuestPublishFragment
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.platform.app.InstrumentationRegistry

@RunWith(AndroidJUnit4::class)
class QuestPublishTest {
    @Test
    fun ContactInputTest() {
        val scenario = launchFragmentInContainer<QuestPublishFragment>()
        //初始状态，没有勾选，全部disabled
        onView(withId(R.id.whatsappInputField))
            .check(matches(isNotEnabled()))
        onView(withId(R.id.instagramInputField))
            .check(matches(isNotEnabled()))

        //勾选后，enabled
        onView(withId(R.id.whatsappCheckBox))
            .perform(click())
        onView(withId(R.id.instagramCheckBox))
            .perform(click())
        onView(withId(R.id.whatsappInputField))
            .check(matches(isEnabled()))
        onView(withId(R.id.instagramInputField))
            .check(matches(isEnabled()))

        

    }

//    @Test
//    fun
}
