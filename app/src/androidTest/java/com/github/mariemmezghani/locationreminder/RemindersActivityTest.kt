package com.github.mariemmezghani.locationreminder

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.github.mariemmezghani.locationreminder.locationreminders.RemindersActivity
import com.github.mariemmezghani.locationreminder.locationreminders.data.ReminderDataSource
import com.github.mariemmezghani.locationreminder.locationreminders.data.local.LocalDB
import com.github.mariemmezghani.locationreminder.locationreminders.data.local.RemindersLocalRepository
import com.github.mariemmezghani.locationreminder.locationreminders.reminderslist.RemindersListViewModel
import com.github.mariemmezghani.locationreminder.locationreminders.savereminder.SaveReminderViewModel
import com.github.mariemmezghani.locationreminder.util.DataBindingIdlingResource
import com.github.mariemmezghani.locationreminder.util.monitorActivity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest

// End to end test
@RunWith(AndroidJUnit4::class)
@LargeTest
class RemindersActivityTest : AutoCloseKoinTest() {
    private lateinit var repository: ReminderDataSource
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setUp() {
        stopKoin()
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    ApplicationProvider.getApplicationContext(),
                    get() as ReminderDataSource
                )
            }
            single {
                SaveReminderViewModel(
                    ApplicationProvider.getApplicationContext(),
                    get() as ReminderDataSource
                )
            }
            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { LocalDB.createRemindersDao(ApplicationProvider.getApplicationContext()) }

        }
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(listOf(myModule))
        }
        repository = GlobalContext.get().koin.get()
        runBlocking {
            repository.deleteAllReminders()
        }
    }

    @After
    fun reset() {
        stopKoin()
    }

    @Before
    fun registerIdlingResources() {
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResources() {
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun authenticationTest_returnAuthenticatedUser() {
        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)
        activityScenario.close()
    }

    @Test
    fun test_reminderAdded_displayedUI() {

        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)
        //no data
        onView(
            withId(R.id.noDataTextView)
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Click FAB
        onView(withId(R.id.addReminderFAB)).perform(click())
        onView(withId(R.id.reminderTitle)).perform(replaceText("test"))
        onView(withId(R.id.reminderDescription)).perform(replaceText("description"))
        // navigate to SelectLocationFragment
        onView(withId(R.id.selectLocation)).perform(click())
        // set marker
        onView(withId(R.id.map)).perform(longClick())
        onView(withId(R.id.save_button)).perform(click())
        // save reminder
        onView(withId(R.id.saveReminder)).perform(click())
        // reminder displayed
        onView(withText("test")).check(ViewAssertions.matches(isDisplayed()))
        onView(withText("description")).check(ViewAssertions.matches(isDisplayed()))
        activityScenario.close()

    }

}
