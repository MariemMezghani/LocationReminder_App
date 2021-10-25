package com.github.mariemmezghani.locationreminder.locationreminders.reminderslist

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.github.mariemmezghani.locationreminder.R
import com.github.mariemmezghani.locationreminder.locationreminders.data.ReminderDataSource
import com.github.mariemmezghani.locationreminder.locationreminders.data.dto.ReminderDTO
import com.github.mariemmezghani.locationreminder.locationreminders.data.local.LocalDB
import com.github.mariemmezghani.locationreminder.locationreminders.data.local.RemindersLocalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.util.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@MediumTest
class ReminderListFragmentTest : AutoCloseKoinTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var repository: ReminderDataSource

    @Before
    fun setUp() {
        stopKoin() // stop the app koin
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    getApplicationContext(),
                    get() as ReminderDataSource
                )
            }
            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { LocalDB.createRemindersDao(getApplicationContext()) }

        }
        startKoin {
            androidContext(getApplicationContext())
            modules(listOf(myModule))
        }
        repository = GlobalContext.get().koin.get()
        runBlocking {
            repository.deleteAllReminders()
        }

    }

    @Test
    fun displayUI() {
        // Given
        val data = ReminderDTO(
            title = "test",
            description = "description",
            location = "location",
            latitude = 0.0,
            longitude = 0.0,
            id = UUID.randomUUID().toString()
        )

        // When
        runBlocking {
            repository.saveReminder(reminder = data)
        }
        launchFragmentInContainer<ReminderListFragment>(
            Bundle.EMPTY,
            R.style.Theme_LocationReminder
        )

        // Then
        onView(ViewMatchers.withText(data.title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun clickFab_navigateToSaveReminderFragment() {
        // Given
        val scenario =
            launchFragmentInContainer<ReminderListFragment>(
                Bundle(),
                R.style.Theme_LocationReminder
            )
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
        // When- clicking FAB
        onView(withId(R.id.addReminderFAB)).perform(click())
        //Then-navigate
        verify(navController).navigate(ReminderListFragmentDirections.actionRemindersFragmentToSaveReminderFragment())


    }
}