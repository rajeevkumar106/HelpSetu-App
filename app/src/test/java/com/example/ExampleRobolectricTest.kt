package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.helpsetu.app.presentation.worker.WorkerViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("HelpSetu", appName)
  }

  @Test
  fun `profile crud operations lifecycle`() {
    val viewModel = WorkerViewModel()

    // 1. Read
    val initialProfiles = viewModel.workers.value
    assertTrue("Should have initial worker profiles", initialProfiles.isNotEmpty())

    // 2. Create
    val created = viewModel.createProfile(
      name = "Rajeev Ranjan Yadav",
      phone = "+919876543299",
      category = "ELECTRICIAN",
      workDetails = "Solar inverter & residential electrical specialist",
      location = "Sector 62, Noida",
      hourlyRate = "₹300/hr",
      experienceYears = 6,
      isAvailable = true
    )
    assertTrue("Profile creation should succeed", created)

    val createdWorker = viewModel.workers.value.find { it.name == "Rajeev Ranjan Yadav" }
    assertNotNull(createdWorker)
    assertEquals("Rajeev Ranjan Yadav", createdWorker?.name)
    assertEquals("+919876543299", createdWorker?.phone)
    val newId = createdWorker!!.id

    // 3. Update
    val updated = viewModel.updateProfile(
      id = newId,
      name = "Rajeev R. Yadav (Senior)",
      phone = "+919876543299",
      category = "ELECTRICIAN",
      workDetails = "Solar inverter & 24x7 emergency wiring specialist",
      location = "Sector 62, Noida",
      hourlyRate = "₹350/hr",
      experienceYears = 7,
      isAvailable = false
    )
    assertTrue("Profile update should succeed", updated)
    val updatedWorker = viewModel.workers.value.find { it.id == newId }
    assertNotNull(updatedWorker)
    assertEquals("Rajeev R. Yadav (Senior)", updatedWorker?.name)
    assertEquals("₹350/hr", updatedWorker?.hourlyRate)
    assertFalse(updatedWorker!!.isAvailable)

    // 4. Toggle Availability
    viewModel.toggleProfileAvailability(newId)
    val toggledWorker = viewModel.workers.value.find { it.id == newId }
    assertTrue(toggledWorker!!.isAvailable)

    // 5. Delete
    val deleted = viewModel.deleteProfile(newId)
    assertTrue("Profile deletion should succeed", deleted)
    assertNull(viewModel.workers.value.find { it.id == newId })
  }
}
