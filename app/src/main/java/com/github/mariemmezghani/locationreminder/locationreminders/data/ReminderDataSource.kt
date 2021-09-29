package com.github.mariemmezghani.locationreminder.locationreminders.data

import com.github.mariemmezghani.locationreminder.locationreminders.data.dto.ReminderDTO
import com.github.mariemmezghani.locationreminder.locationreminders.data.dto.Result



/**
 * Main entry point for accessing reminders data.
 */
interface ReminderDataSource {
    suspend fun getReminders(): Result<List<ReminderDTO>>
    suspend fun saveReminder(reminder: ReminderDTO)
    suspend fun getReminder(id: String): Result<ReminderDTO>
    suspend fun deleteAllReminders()
}