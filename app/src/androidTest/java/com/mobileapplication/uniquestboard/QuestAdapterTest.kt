package com.mobileapplication.uniquestboard
import android.view.ViewGroup
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
import com.mobileapplication.uniquestboard.ui.common.QuestListAdapter
import com.mobileapplication.uniquestboard.ui.common.Status
import com.mobileapplication.uniquestboard.ui.questDetail.QuestDetailFragment
import com.mobileapplication.uniquestboard.ui.questPublish.QuestPublishFragment
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import java.lang.Thread.sleep
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.function.Predicate.not


@RunWith(AndroidJUnit4::class)
class QuestAdapterTest {
    @Test
    fun testOnCreateViewHolder() {
        val adapter = QuestListAdapter(emptyList())
        val parent = mock(ViewGroup::class.java)

        val viewHolder = adapter.onCreateViewHolder(parent, 0)
        assertNotNull(viewHolder)
    }
}