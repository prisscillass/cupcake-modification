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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.cupcake.R
import com.example.cupcake.data.OrderUiState
import com.example.cupcake.ui.components.FormattedPriceLabel
import com.example.cupcake.ui.theme.CupcakeTheme

/**
 * Layar ringkasan pesanan komputer/notebook:
 * Menampilkan semua spesifikasi yang dipilih dan perkiraan total harga.
 */
@Composable
fun OrderSummaryScreen(
    orderUiState: OrderUiState,
    onCancelButtonClicked: () -> Unit,
    onSendButtonClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val processorText = orderUiState.processor.ifEmpty { orderUiState.flavor }
    val monitorText = orderUiState.monitor.ifEmpty { orderUiState.date }

    // Format teks detail pesanan untuk dibagikan / dikirim
    val orderSummary = stringResource(
        R.string.order_details,
        orderUiState.deviceType.ifEmpty { "-" },
        processorText.ifEmpty { "-" },
        orderUiState.ram.ifEmpty { "-" },
        orderUiState.storage.ifEmpty { "-" },
        orderUiState.graphics.ifEmpty { "-" },
        monitorText.ifEmpty { "-" },
        orderUiState.price
    )
    val newOrder = stringResource(R.string.new_computer_order)

    // Daftar rincian spesifikasi untuk ditampilkan pada layar
    val items = listOf(
        Pair(stringResource(R.string.device_type_label), orderUiState.deviceType),
        Pair(stringResource(R.string.processor_label), processorText),
        Pair(stringResource(R.string.ram_label), orderUiState.ram),
        Pair(stringResource(R.string.storage_label), orderUiState.storage),
        Pair(stringResource(R.string.graphics_label), orderUiState.graphics),
        Pair(stringResource(R.string.monitor_label), monitorText)
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            items.forEach { item ->
                Text(
                    text = item.first.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = item.second.ifEmpty { "-" },
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Divider(thickness = dimensionResource(R.dimen.thickness_divider))
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            FormattedPriceLabel(
                subtotal = orderUiState.price,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Row(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSendButtonClicked(newOrder, orderSummary) }
                ) {
                    Text(stringResource(R.string.send))
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancelButtonClicked
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }
}

@Preview
@Composable
fun OrderSummaryPreview() {
    CupcakeTheme {
        OrderSummaryScreen(
            orderUiState = OrderUiState(
                deviceType = "Notebook / Laptop",
                processor = "Intel Core i5-13400",
                ram = "16 GB DDR5 Dual Channel",
                storage = "SSD NVMe M.2 1 TB PCIe 4.0",
                graphics = "NVIDIA GeForce RTX 4060 8 GB GDDR6",
                monitor = "14\" FHD IPS Display",
                price = "Rp 16.500.000"
            ),
            onSendButtonClicked = { _, _ -> },
            onCancelButtonClicked = {},
            modifier = Modifier.fillMaxHeight()
        )
    }
}
