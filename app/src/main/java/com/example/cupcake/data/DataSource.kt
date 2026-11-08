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

import com.example.cupcake.R

object DataSource {

    /**
     * Pilihan tipe perangkat komputer/notebook di layar awal beserta harga awal (base price).
     */
    val deviceOptions = listOf(
        Pair(R.string.device_notebook, 5500000L),
        Pair(R.string.device_desktop, 4500000L),
        Pair(R.string.device_gaming, 8500000L)
    )

    /**
     * Opsi jumlah untuk backward compatibility dengan pengujian sebelumnya.
     */
    val quantityOptions = listOf(
        Pair(R.string.device_notebook, 1),
        Pair(R.string.device_desktop, 6),
        Pair(R.string.device_gaming, 12)
    )

    /**
     * Pilihan 1: Jenis Prosesor
     */
    val processors = listOf(
        R.string.proc_i3,
        R.string.proc_r5,
        R.string.proc_i5,
        R.string.proc_r7,
        R.string.proc_i7
    )

    /**
     * Pilihan 2: Besar Memori (RAM)
     */
    val memoryOptions = listOf(
        R.string.ram_8gb,
        R.string.ram_16gb,
        R.string.ram_32gb,
        R.string.ram_64gb
    )

    /**
     * Pilihan 3: Ukuran dan Tipe Hardisk (Storage)
     */
    val storageOptions = listOf(
        R.string.storage_512gb_ssd,
        R.string.storage_1tb_ssd,
        R.string.storage_2tb_ssd,
        R.string.storage_dual
    )

    /**
     * Pilihan 4: Tipe Kartu Grafis (GPU)
     */
    val graphicsOptions = listOf(
        R.string.gpu_integrated,
        R.string.gpu_gtx1650,
        R.string.gpu_rtx3060,
        R.string.gpu_rtx4060,
        R.string.gpu_rtx4070
    )

    /**
     * Pilihan 5: Ukuran Monitor
     */
    val monitorOptions = listOf(
        R.string.monitor_14_fhd,
        R.string.monitor_22_fhd,
        R.string.monitor_24_144hz,
        R.string.monitor_27_2k,
        R.string.monitor_32_4k
    )

    /**
     * Alias untuk backward compatibility
     */
    val flavors = processors
}
