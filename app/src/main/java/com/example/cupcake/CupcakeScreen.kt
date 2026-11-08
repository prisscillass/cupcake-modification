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
package com.example.cupcake

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cupcake.data.DataSource
import com.example.cupcake.data.OrderUiState
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.OrderViewModel
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.ui.StartOrderScreen

/**
 * Enum values yang merepresentasikan tahapan layar konfigurasi komputer:
 * Start -> Processor -> Ram -> Storage -> Graphics -> Monitor -> Summary
 */
enum class CupcakeScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Processor(title = R.string.choose_processor),
    Ram(title = R.string.choose_ram),
    Storage(title = R.string.choose_storage),
    Graphics(title = R.string.choose_graphics),
    Monitor(title = R.string.choose_monitor),
    Summary(title = R.string.order_summary);

    companion object {
        val Flavor = Processor
        val Pickup = Monitor
    }
}

/**
 * Composable AppBar yang menampilkan judul layar saat ini dan tombol kembali (back button).
 */
@Composable
fun CupcakeAppBar(
    currentScreen: CupcakeScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun CupcakeApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Ambil back stack entry saat ini
    val backStackEntry by navController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination?.route ?: CupcakeScreen.Start.name
    val currentScreen = try {
        CupcakeScreen.valueOf(route)
    } catch (e: Exception) {
        CupcakeScreen.Start
    }

    Scaffold(
        topBar = {
            CupcakeAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        val context = LocalContext.current

        NavHost(
            navController = navController,
            startDestination = CupcakeScreen.Start.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            // Layar 1: Pemilihan Perangkat (Notebook / PC Desktop / Gaming)
            composable(route = CupcakeScreen.Start.name) {
                StartOrderScreen(
                    deviceOptions = DataSource.deviceOptions,
                    onNextButtonClicked = { deviceName, basePrice ->
                        viewModel.setDeviceType(deviceName, basePrice)
                        navController.navigate(CupcakeScreen.Processor.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }

            // Layar 2: Pemilihan Jenis Prosesor
            composable(route = CupcakeScreen.Processor.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = DataSource.processors.map { id -> context.resources.getString(id) },
                    initialSelection = uiState.processor,
                    onSelectionChanged = { viewModel.setProcessor(it) },
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Ram.name) },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }

            // Layar 3: Pemilihan Besar Memori (RAM)
            composable(route = CupcakeScreen.Ram.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = DataSource.memoryOptions.map { id -> context.resources.getString(id) },
                    initialSelection = uiState.ram,
                    onSelectionChanged = { viewModel.setRam(it) },
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Storage.name) },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }

            // Layar 4: Pemilihan Ukuran dan Tipe Hardisk (Storage)
            composable(route = CupcakeScreen.Storage.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = DataSource.storageOptions.map { id -> context.resources.getString(id) },
                    initialSelection = uiState.storage,
                    onSelectionChanged = { viewModel.setStorage(it) },
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Graphics.name) },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }

            // Layar 5: Pemilihan Tipe Kartu Grafis (GPU)
            composable(route = CupcakeScreen.Graphics.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = DataSource.graphicsOptions.map { id -> context.resources.getString(id) },
                    initialSelection = uiState.graphics,
                    onSelectionChanged = { viewModel.setGraphics(it) },
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Monitor.name) },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }

            // Layar 6: Pemilihan Ukuran Monitor
            composable(route = CupcakeScreen.Monitor.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = DataSource.monitorOptions.map { id -> context.resources.getString(id) },
                    initialSelection = uiState.monitor,
                    onSelectionChanged = { viewModel.setMonitor(it) },
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Summary.name) },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }

            // Layar 7: Ringkasan Spesifikasi & Perkiraan Harga Total
            composable(route = CupcakeScreen.Summary.name) {
                OrderSummaryScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    onSendButtonClicked = { subject: String, summary: String ->
                        shareOrder(context, subject = subject, summary = summary)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

/**
 * Mereset [OrderUiState] dan kembali ke layar awal [CupcakeScreen.Start]
 */
private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(CupcakeScreen.Start.name, inclusive = false)
}

/**
 * Membuat Intent ACTION_SEND untuk membagikan/mengirim detail pesanan komputer ke aplikasi lain
 */
private fun shareOrder(context: Context, subject: String, summary: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.new_computer_order)
        )
    )
}
