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
package com.example.cupcake.data

/**
 * Data class yang merepresentasikan state pemilihan komputer/notebook:
 * [deviceType], [processor], [ram], [storage], [graphics], [monitor], dan perkiraan [price].
 */
data class OrderUiState(
    /** Tipe komputer / notebook yang dipilih (misal: "Notebook / Laptop", "Komputer PC Desktop") */
    val deviceType: String = "",
    /** Harga dasar tipe perangkat */
    val basePrice: Long = 0L,
    /** Pilihan 1: Jenis prosesor */
    val processor: String = "",
    val processorPrice: Long = 0L,
    /** Pilihan 2: Besar memori (RAM) */
    val ram: String = "",
    val ramPrice: Long = 0L,
    /** Pilihan 3: Ukuran dan tipe hardisk (Storage) */
    val storage: String = "",
    val storagePrice: Long = 0L,
    /** Pilihan 4: Tipe kartu grafis (GPU) */
    val graphics: String = "",
    val graphicsPrice: Long = 0L,
    /** Pilihan 5: Ukuran monitor */
    val monitor: String = "",
    val monitorPrice: Long = 0L,
    /** Perkiraan total harga yang telah diformat (misal: "Rp 15.500.000") */
    val price: String = "Rp 0",

    // Backward compatibility fields:
    val quantity: Int = 1,
    val flavor: String = "",
    val date: String = "",
    val pickupOptions: List<String> = listOf()
)
