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
package com.example.cupcake.ui

import androidx.lifecycle.ViewModel
import com.example.cupcake.data.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * [OrderViewModel] menyimpan informasi pemilihan spesifikasi komputer/notebook:
 * Tipe perangkat, jenis prosesor, besar memori (RAM), ukuran & tipe hardisk,
 * tipe kartu grafis (GPU), dan ukuran monitor.
 * ViewModel ini juga menghitung perkiraan total harga komputer secara otomatis.
 */
class OrderViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState(pickupOptions = pickupOptions()))
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    /**
     * Memilih tipe perangkat komputer/notebook dan harga dasar awalnya.
     */
    fun setDeviceType(deviceType: String, basePrice: Long) {
        _uiState.update { currentState ->
            val updated = currentState.copy(
                deviceType = deviceType,
                basePrice = basePrice,
                quantity = 1
            )
            updated.copy(price = calculatePrice(updated))
        }
    }

    /**
     * Pilihan 1: Menentukan jenis prosesor
     */
    fun setProcessor(desiredProcessor: String) {
        val procPrice = getProcessorPrice(desiredProcessor)
        _uiState.update { currentState ->
            val updated = currentState.copy(
                processor = desiredProcessor,
                processorPrice = procPrice,
                flavor = desiredProcessor
            )
            updated.copy(price = calculatePrice(updated))
        }
    }

    /**
     * Pilihan 2: Menentukan besar memori (RAM)
     */
    fun setRam(desiredRam: String) {
        val ramPrice = getRamPrice(desiredRam)
        _uiState.update { currentState ->
            val updated = currentState.copy(
                ram = desiredRam,
                ramPrice = ramPrice
            )
            updated.copy(price = calculatePrice(updated))
        }
    }

    /**
     * Pilihan 3: Menentukan ukuran dan tipe hardisk (Storage)
     */
    fun setStorage(desiredStorage: String) {
        val storagePrice = getStoragePrice(desiredStorage)
        _uiState.update { currentState ->
            val updated = currentState.copy(
                storage = desiredStorage,
                storagePrice = storagePrice
            )
            updated.copy(price = calculatePrice(updated))
        }
    }

    /**
     * Pilihan 4: Menentukan tipe kartu grafis (GPU)
     */
    fun setGraphics(desiredGraphics: String) {
        val graphicsPrice = getGraphicsPrice(desiredGraphics)
        _uiState.update { currentState ->
            val updated = currentState.copy(
                graphics = desiredGraphics,
                graphicsPrice = graphicsPrice
            )
            updated.copy(price = calculatePrice(updated))
        }
    }

    /**
     * Pilihan 5: Menentukan ukuran monitor
     */
    fun setMonitor(desiredMonitor: String) {
        val monitorPrice = getMonitorPrice(desiredMonitor)
        _uiState.update { currentState ->
            val updated = currentState.copy(
                monitor = desiredMonitor,
                monitorPrice = monitorPrice,
                date = desiredMonitor
            )
            updated.copy(price = calculatePrice(updated))
        }
    }

    /**
     * Reset order state ke nilai awal
     */
    fun resetOrder() {
        _uiState.value = OrderUiState(pickupOptions = pickupOptions())
    }

    /**
     * Menghitung perkiraan total harga komputer/notebook berdasarkan komponen yang dipilih.
     */
    private fun calculatePrice(state: OrderUiState): String {
        val total = state.basePrice +
                state.processorPrice +
                state.ramPrice +
                state.storagePrice +
                state.graphicsPrice +
                state.monitorPrice

        val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
        return "Rp " + formatter.format(total)
    }

    /** Harga komponen berdasarkan pilihan */
    private fun getProcessorPrice(processor: String): Long {
        return when {
            processor.contains("i3", ignoreCase = true) -> 1_600_000L
            processor.contains("Ryzen 5", ignoreCase = true) || processor.contains("5600", ignoreCase = true) -> 2_200_000L
            processor.contains("i5", ignoreCase = true) || processor.contains("13400", ignoreCase = true) -> 3_400_000L
            processor.contains("Ryzen 7", ignoreCase = true) || processor.contains("7700", ignoreCase = true) -> 4_800_000L
            processor.contains("i7", ignoreCase = true) || processor.contains("13700", ignoreCase = true) -> 6_500_000L
            else -> 1_800_000L
        }
    }

    private fun getRamPrice(ram: String): Long {
        return when {
            ram.contains("8 GB", ignoreCase = true) -> 400_000L
            ram.contains("16 GB", ignoreCase = true) -> 850_000L
            ram.contains("32 GB", ignoreCase = true) -> 1_700_000L
            ram.contains("64 GB", ignoreCase = true) -> 3_400_000L
            else -> 500_000L
        }
    }

    private fun getStoragePrice(storage: String): Long {
        return when {
            storage.contains("Dual", ignoreCase = true) -> 1_350_000L
            storage.contains("512 GB", ignoreCase = true) -> 600_000L
            storage.contains("1 TB", ignoreCase = true) -> 1_150_000L
            storage.contains("2 TB", ignoreCase = true) -> 2_200_000L
            else -> 700_000L
        }
    }

    private fun getGraphicsPrice(graphics: String): Long {
        return when {
            graphics.contains("Integrated", ignoreCase = true) -> 0L
            graphics.contains("1650", ignoreCase = true) -> 2_200_000L
            graphics.contains("3060", ignoreCase = true) -> 4_500_000L
            graphics.contains("4060", ignoreCase = true) -> 5_500_000L
            graphics.contains("4070", ignoreCase = true) -> 10_200_000L
            else -> 0L
        }
    }

    private fun getMonitorPrice(monitor: String): Long {
        return when {
            monitor.contains("14\"", ignoreCase = true) -> 0L
            monitor.contains("22\"", ignoreCase = true) -> 1_250_000L
            monitor.contains("24\"", ignoreCase = true) -> 1_850_000L
            monitor.contains("27\"", ignoreCase = true) -> 3_200_000L
            monitor.contains("32\"", ignoreCase = true) -> 5_600_000L
            else -> 0L
        }
    }

    // --- Backward compatibility methods ---
    fun setQuantity(numberCupcakes: Int) {
        val basePrice = when (numberCupcakes) {
            1 -> 5500000L
            6 -> 4500000L
            else -> 8500000L
        }
        val deviceName = when (numberCupcakes) {
            1 -> "Notebook / Laptop"
            6 -> "Komputer PC Desktop"
            else -> "PC / Notebook Gaming"
        }
        setDeviceType(deviceName, basePrice)
    }

    fun setFlavor(desiredFlavor: String) {
        setProcessor(desiredFlavor)
    }

    fun setDate(pickupDate: String) {
        setMonitor(pickupDate)
    }

    private fun pickupOptions(): List<String> {
        val dateOptions = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4) {
            dateOptions.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return dateOptions
    }
}
