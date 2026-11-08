/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.cupcake.CupcakeApp
import com.example.cupcake.CupcakeScreen
import com.example.cupcake.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CupcakeScreenNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupCupcakeNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            CupcakeApp(navController = navController)
        }
    }

    @Test
    fun cupcakeNavHost_verifyStartDestination() {
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    @Test
    fun cupcakeNavHost_verifyBackNavigationNotShownOnStartOrderScreen() {
        val backText = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist()
    }

    @Test
    fun cupcakeNavHost_clickDevice_navigatesToProcessorScreen() {
        composeTestRule.onNodeWithStringId(R.string.device_notebook)
            .performClick()
        navController.assertCurrentRouteName(CupcakeScreen.Processor.name)
    }

    @Test
    fun cupcakeNavHost_clickNextOnProcessorScreen_navigatesToRamScreen() {
        navigateToProcessorScreen()
        composeTestRule.onNodeWithStringId(R.string.next)
            .performClick()
        navController.assertCurrentRouteName(CupcakeScreen.Ram.name)
    }

    @Test
    fun cupcakeNavHost_clickBackOnProcessorScreen_navigatesToStartOrderScreen() {
        navigateToProcessorScreen()
        performNavigateUp()
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    @Test
    fun cupcakeNavHost_clickCancelOnProcessorScreen_navigatesToStartOrderScreen() {
        navigateToProcessorScreen()
        composeTestRule.onNodeWithStringId(R.string.cancel)
            .performClick()
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    @Test
    fun cupcakeNavHost_fullFlowToSummaryScreen() {
        navigateToProcessorScreen()
        // Ke RAM
        composeTestRule.onNodeWithStringId(R.string.next).performClick()
        composeTestRule.onNodeWithStringId(R.string.ram_16gb).performClick()
        // Ke Storage
        composeTestRule.onNodeWithStringId(R.string.next).performClick()
        composeTestRule.onNodeWithStringId(R.string.storage_1tb_ssd).performClick()
        // Ke Graphics
        composeTestRule.onNodeWithStringId(R.string.next).performClick()
        composeTestRule.onNodeWithStringId(R.string.gpu_rtx4060).performClick()
        // Ke Monitor
        composeTestRule.onNodeWithStringId(R.string.next).performClick()
        composeTestRule.onNodeWithStringId(R.string.monitor_24_144hz).performClick()
        // Ke Summary
        composeTestRule.onNodeWithStringId(R.string.next).performClick()
        navController.assertCurrentRouteName(CupcakeScreen.Summary.name)
    }

    @Test
    fun cupcakeNavHost_clickCancelOnSummaryScreen_navigatesToStartOrderScreen() {
        cupcakeNavHost_fullFlowToSummaryScreen()
        composeTestRule.onNodeWithStringId(R.string.cancel)
            .performClick()
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }

    private fun navigateToProcessorScreen() {
        composeTestRule.onNodeWithStringId(R.string.device_notebook)
            .performClick()
        composeTestRule.onNodeWithStringId(R.string.proc_i5)
            .performClick()
    }

    private fun performNavigateUp() {
        val backText = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).performClick()
    }
}
