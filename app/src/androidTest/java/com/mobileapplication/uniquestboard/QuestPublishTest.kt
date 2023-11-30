package com.mobileapplication.uniquestboard

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.mobileapplication.uniquestboard.ui.questPublish.QuestPublishFragment
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep


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
        scenario.onFragment { fragment ->
            assertFalse(fragment.generateContact())
        }
        //勾选后，enabled
        onView(withId(R.id.whatsappCheckBox))
            .perform(click())
        onView(withId(R.id.instagramCheckBox))
            .perform(click())
        onView(withId(R.id.whatsappInputField))
            .check(matches(isEnabled()))
        onView(withId(R.id.instagramInputField))
            .check(matches(isEnabled()))

        //空状态点publish
        scenario.onFragment { fragment ->
            assertFalse(fragment.generateContact())
        }
        onView(withId(R.id.whatsappInputField)).perform(typeText("68887777"))
        scenario.onFragment { fragment ->
            assertFalse(fragment.generateContact())
            assertEquals("68887777",fragment.viewModel.whatsapp)
        }
        onView(withId(R.id.instagramInputField)).perform(typeText("@myself"))

        scenario.onFragment { fragment ->
            assertTrue(fragment.generateContact())
            assertEquals("@myself",fragment.viewModel.instagram)
        }

        onView(withId(R.id.instagramCheckBox))
            .perform(click())

        scenario.onFragment { fragment ->
            assertTrue(fragment.generateContact())
        }
    }

    @Test
    fun titleContentRewardTest(){
        val scenario = launchFragmentInContainer<QuestPublishFragment>()
        GlobalVariables.user = User("tester","111223")
        onView(withId(R.id.whatsappCheckBox))
            .perform(click())
        onView(withId(R.id.instagramCheckBox))
            .perform(click())
        onView(withId(R.id.whatsappInputField)).perform(typeText("66667777"))
        onView(withId(R.id.instagramInputField)).perform(typeText("@myself"))
        scenario.onFragment { fragment ->
            assertTrue(fragment.generateContact())
        }
        scenario.onFragment { fragment ->
            assertFalse(fragment.generateQuest())
        }
        onView(withId(R.id.title)).perform(typeText("This is a title lalalalalalalalalalalalalalalal"))
        scenario.onFragment { fragment ->
            assertFalse(fragment.generateQuest())
            assertEquals("This is a title lalalalalalalalalalalalalalalal",fragment.viewModel.title)
        }
        onView(withId(R.id.content)).perform(typeText("This is the content fafafafafafafafafffffffffffaaaaaaaaaaaaaffffffffffffffffffffffffffffffffaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaafffffffffffffffffffffffffffffffffffffffffffaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaafffffffffffffffffffffffffffffffffaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"))
        scenario.onFragment { fragment ->
            assertTrue(fragment.generateQuest()) // Reward有默认值
            assertEquals("This is the content fafafafafafafafafffffffffffaaaaaaaaaaaaaffffffffffffffffffffffffffffffffaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaafffffffffffffffffffffffffffffffffffffffffffaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaafffffffffffffffffffffffffffffffffaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                ,fragment.viewModel.content)
            assertEquals("Gratitude",fragment.viewModel.reward)
        }
        onView(withId(R.id.reward)).perform(typeText("This is the reward"))

        scenario.onFragment { fragment ->
            assertTrue(fragment.generateQuest())
            assertEquals("GratitudeThis is the reward",fragment.viewModel.reward)
        }
    }

    @Test
    fun DateTimePickerTest(){
        val scenario = launchFragmentInContainer<QuestPublishFragment>()
        onView(withId(R.id.dateEditText)).perform(click())
        onView(withClassName(Matchers.equalTo<String>(DatePicker::class.java.name))).perform(
            PickerActions.setDate(2024, 1, 1)
        )
        //onView(withClassName(Matchers.equalTo(DatePicker::class.java.name))).inRoot(isDialog()).perform(PickerActions.setDate(2023, 1, 1))
        onView(withText("OK"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(R.id.dateEditText)).check(matches(withText("2024/01/01")))

        // 测试 TimePicker
        onView(withId(R.id.timeEditText)).perform(click())
        onView(isAssignableFrom(TimePicker::class.java)).inRoot(isDialog()).perform(PickerActions.setTime(12, 0))
        onView(withText("OK"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
        sleep(1000)
        onView(withId(R.id.timeEditText)).check(matches(withText("12:00")))
    }
}
