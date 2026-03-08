package com.noted.app.presentation.notes

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.core.R
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noted.app.data.local.dao.NoteDao
import com.noted.app.data.local.entity.NoteEntity
import com.noted.app.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NotesScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var noteDao: NoteDao

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking {
            noteDao.deleteAllNotes()
        }
    }

    @Test
    fun search_displaysMatchingNotes() {
        runBlocking {
            noteDao.insertNote(
                NoteEntity(
                    title = "Shopping List",
                    content = "Milk, Eggs, Bread",
                    color = 0xFFFFFFFF.toInt(),
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    isPinned = false
                )
            )
        }

        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNode(hasText("Search notes")).performTextInput("Shopping")
        composeTestRule.waitForIdle()

        Thread.sleep(500)

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Shopping List").assertIsDisplayed()
    }

    @Test
    fun search_filtersOutNonMatchingNotes() {
        runBlocking {
            noteDao.insertNote(
                NoteEntity(
                    title = "Shopping List",
                    content = "Milk, Eggs, Bread",
                    color = 0xFFFFFFFF.toInt(),
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    isPinned = false
                )
            )

            noteDao.insertNote(
                NoteEntity(
                    title = "Meeting notes",
                    content = "Discuss project timeline",
                    color = 0xFFFFFFFF.toInt(),
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    isPinned = false
                )
            )
        }

        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNode(hasText("Search notes")).performTextInput("Meeting")
        composeTestRule.waitForIdle()

        Thread.sleep(500)
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Meeting notes").assertIsDisplayed()
        composeTestRule.onNodeWithText("Shopping List").assertDoesNotExist()

    }

    @Test
    fun search_byContentOnly_displaysMatchingNote() {
        runBlocking {
            noteDao.insertNote(
                NoteEntity(
                    title = "Shopping List",
                    content = "Milk, Eggs, Bread",
                    color = 0xFFFFFFFF.toInt(),
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    isPinned = false
                )
            )

            noteDao.insertNote(
                NoteEntity(
                    title = "Meeting notes",
                    content = "Discuss project timeline",
                    color = 0xFFFFFFFF.toInt(),
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    isPinned = false
                )
            )
        }

        composeTestRule.onNodeWithContentDescription("Search").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNode(hasText("Search notes")).performTextInput("Milk")
        composeTestRule.waitForIdle()

        Thread.sleep(500)
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Meeting notes").assertDoesNotExist()
        composeTestRule.onNodeWithText("Shopping List").assertIsDisplayed()

    }



}