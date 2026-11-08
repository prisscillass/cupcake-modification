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
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.cupcake.R
import com.example.cupcake.data.DataSource
import com.example.cupcake.data.OrderUiState
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.ui.StartOrderScreen
import org.junit.Rule
import org.junit.Test

class CupcakeOrderScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fakeOrderUiState = OrderUiState(
        deviceType = "Notebook / Laptop",
        processor = "Intel Core i5-13400",
        ram = "16 GB DDR5",
        storage = "1 TB SSD NVMe",
        graphics = "NVIDIA GeForce RTX 4060",
        monitor = "24\" FHD 144Hz",
        price = "Rp 16.500.000",
        flavor = "Intel Core i5-13400",
        date = "24\" FHD 144Hz"
    )

    /**
     * Memverifikasi opsi tipe perangkat tampil di layar awal (StartOrderScreen).
     */
    @Test
    fun startOrderScreen_verifyContent() {
        composeTestRule.setContent {
            StartOrderScreen(
                deviceOptions = DataSource.deviceOptions,
                onNextButtonClicked = { _, _ -> }
            )
        }

        DataSource.deviceOptions.forEach {
            composeTestRule.onNodeWithStringId(it.first).assertIsDisplayed()
        }
    }

    /**
     * Memverifikasi opsi dan perkiraan harga sementara pada SelectOptionScreen.
     */
    @Test
    fun selectOptionScreen_verifyContent() {
        val options = listOf("Opsi A", "Opsi B", "Opsi C")
        val subtotal = "Rp 10.000.000"

        composeTestRule.setContent {
            SelectOptionScreen(subtotal = subtotal, options = options)
        }

        options.forEach { option ->
            composeTestRule.onNodeWithText(option).assertIsDisplayed()
        }

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.subtotal_price,
                subtotal
            )
        ).assertIsDisplayed()

        composeTestRule.onNodeWithStringId(R.string.next).assertIsNotEnabled()
    }

    /**
     * Memverifikasi tombol lanjut aktif ketika salah satu opsi dipilih.
     */
    @Test
    fun selectOptionScreen_optionSelected_NextButtonEnabled() {
        val options = listOf("Opsi A", "Opsi B", "Opsi C")
        val subTotal = "Rp 10.000.000"

        composeTestRule.setContent {
            SelectOptionScreen(subtotal = subTotal, options = options)
        }

        composeTestRule.onNodeWithText("Opsi A").performClick()
        composeTestRule.onNodeWithStringId(R.string.next).assertIsEnabled()
    }

    /**
     * Memverifikasi bahwa data pesanan ditampilkan dengan benar pada OrderSummaryScreen.
     */
    @Test
    fun summaryScreen_verifyContentDisplay() {
        composeTestRule.setContent {
            OrderSummaryScreen(
                orderUiState = fakeOrderUiState,
                onCancelButtonClicked = {},
                onSendButtonClicked = { _, _ -> },
            )
        }

        composeTestRule.onNodeWithText(fakeOrderUiState.processor).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeOrderUiState.monitor).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.subtotal_price,
                fakeOrderUiState.price
            )
        ).assertIsDisplayed()
    }
}
