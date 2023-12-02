package com.mobileapplication.uniquestboard
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.mobileapplication.uniquestboard.ui.common.Contact
import com.mobileapplication.uniquestboard.ui.common.Quest
import com.mobileapplication.uniquestboard.ui.common.Status
import com.mobileapplication.uniquestboard.ui.questDetail.QuestDetailFragment
import com.mobileapplication.uniquestboard.ui.questPublish.QuestPublishFragment
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.function.Predicate.not


@RunWith(AndroidJUnit4::class)
class QuestDetailTest {
    val df = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
    @Test
    fun QuestContentTest(){
        val scenario = launchFragmentInContainer<QuestDetailFragment>()
        val testTime = LocalDateTime.now()

        scenario.onFragment { fragment ->
            fragment.viewModel.curQuest = Quest(
                LocalDateTime.now(),
                LocalDateTime.now(),
                "tester",
                "taker",
                "title hahahahahahaha",
                "content xixixixxixixixi",
                Status.PENDING,
                mutableListOf(),
                "reward da",
                Contact("4568765","@tester"),
                "testquestID"
            )
            fragment.RefreshUI()
        }
        onView(withId(R.id.tvTitle)).check(matches(withText("title hahahahahahaha")))
        onView(withId(R.id.tvContent)).check(matches(withText("content xixixixxixixixi")))
        onView(withId(R.id.tvReward)).check(matches(withText("Reward : reward da")))
        onView(withId(R.id.tvPublishTime)).check(matches(withText("Published : " + df.format(testTime))))
        onView(withId(R.id.tvExpireTime)).check(matches(withText("Expire : " + df.format(testTime))))
        onView(withId(R.id.contactInformationContainer)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        scenario.onFragment { fragment ->
            fragment.viewModel.curQuest!!.publisher = "someoneElse"
            fragment.RefreshUI()
        }
        onView(withId(R.id.contactInformationContainer)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        scenario.onFragment { fragment ->
            fragment.viewModel.curQuest!!.taker="tester"
            fragment.RefreshUI()
        }
        onView(withId(R.id.contactInformationContainer)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun StatusButtonTest(){
        val scenario = launchFragmentInContainer<QuestDetailFragment>()
        val testTime = LocalDateTime.now()
        scenario.onFragment { fragment ->
            fragment.viewModel.curQuest = Quest(
                LocalDateTime.now(),
                LocalDateTime.now(),
                "tester",
                "taker",
                "title hahahahahahaha",
                "content xixixixxixixixi",
                Status.PENDING,
                mutableListOf(),
                "reward da",
                Contact("4568765","@tester"),
                "testquestID"
            )
            fragment.RefreshUI()
        }
        //As publisher
        onView(withId(R.id.cancelButton)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.completeButton)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.takeButton)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        scenario.onFragment { fragment ->
            fragment.viewModel.curQuest!!.publisher = "someoneElse"
            fragment.RefreshUI()
        }
        //As potential taker
        onView(withId(R.id.cancelButton)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.completeButton)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.takeButton)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        //As taker
        scenario.onFragment { fragment ->
            fragment.viewModel.curQuest!!.taker="tester"
            fragment.RefreshUI()
        }
        onView(withId(R.id.cancelButton)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.completeButton)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.takeButton)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        scenario.onFragment { fragment ->
            fragment.viewModel.curQuest!!.status = Status.EXPIRED
            fragment.RefreshUI()
        }
        onView(withId(R.id.cancelButton)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.completeButton)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.takeButton)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}