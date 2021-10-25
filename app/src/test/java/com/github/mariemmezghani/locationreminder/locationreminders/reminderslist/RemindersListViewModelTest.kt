package com.github.mariemmezghani.locationreminder.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mariemmezghani.locationreminder.MainCoroutineRule
import com.github.mariemmezghani.locationreminder.data.FakeDataSource
import com.github.mariemmezghani.locationreminder.getOrAwaitValue
import com.github.mariemmezghani.locationreminder.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest{
    @get:Rule
    var instantTaskExecutorRule= InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    private lateinit var dataSource: FakeDataSource
    private lateinit var viewModel: RemindersListViewModel
    @Before
    fun setUpViewModel() {
        stopKoin()
        dataSource = FakeDataSource()
        viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(), dataSource)
    }
    @Test
    fun test_emptyList() = runBlockingTest {

        dataSource.deleteAllReminders()
        viewModel.loadReminders()

        assertThat(viewModel.showNoData.value, CoreMatchers.`is`(true))

    }

    @Test
    fun test_addReminder_showLoading() = runBlockingTest {
        val reminder = ReminderDTO(
            title = "test",
            description = "desc",
            location = "location",
            latitude = 0.0,
            longitude = 0.0)

        dataSource.saveReminder(reminder)
        mainCoroutineRule.pauseDispatcher()
        viewModel.loadReminders()

        assertThat(viewModel.showLoading.value, CoreMatchers.`is`(true))
    }
    @Test
    fun test_error_showSnackbar() = runBlockingTest {

        dataSource.setShouldReturnError(true)
        viewModel.loadReminders()
        assertThat(viewModel.showSnackBar.getOrAwaitValue(), CoreMatchers.`is`("reminders not found"))

    }

}