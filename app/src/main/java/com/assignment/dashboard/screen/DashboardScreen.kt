package com.assignment.dashboard.screen

import DashboardViewModel
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.assignment.dashboard.R
import com.assignment.dashboard.data.exportDataToPdf

@Composable
fun DashboardScreen(
    isTablet: Boolean, 
    viewModel: DashboardViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    val icons = listOf(
        R.drawable.ic_home,
        R.drawable.ic_game,
        R.drawable.ic_document,
        R.drawable.ic_rectangle,
        R.drawable.ic_setting
    )
    
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val tabIconSize = (screenWidth * 0.04f).dp
    val mobileIconSize = (screenWidth * 0.08f).dp

    val pdfExportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        if (uri != null) {
            exportDataToPdf(context, uri, state)
        }
    }

    val onExportClicked = {
        pdfExportLauncher.launch("PerformanceReport_${state.selectedSession}.pdf")
    }
    
    Column(modifier = modifier.fillMaxSize()){
        if (isTablet) {
            Row(modifier = Modifier.fillMaxSize()) {
                SidebarUi(modifier = Modifier.width(100.dp).fillMaxHeight(),
                    icons = icons,
                    iconSize = tabIconSize,
                    isHorizontal = false,
                    onIconClick = {}
                )

                Card(
                    modifier = Modifier.padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ){
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                    ) {
                        HeaderAndMetricsUi(
                            state, 
                            true, 
                            onExportClicked = onExportClicked, 
                            onDateSelected = { newDate -> viewModel.onDateSelected(newDate) },
                            onSessionSelected = { newSession -> viewModel.onSessionChanged(newSession) }
                        )

                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            HandOverlayUi(
                                joints = state.handJoints,
                                onJointClicked = { jointId ->
                                    Log.d("HandOverlayUi", "Joint clicked: $jointId")
                                },
                                modifier = Modifier
                                    .weight(0.4f)
                                    .fillMaxHeight()
                            )
                            GraphsUi(
                                state,
                                modifier = Modifier
                                    .weight(0.6f)
                                    .fillMaxHeight()
                                    .padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                SidebarUi(modifier = Modifier.height(70.dp).fillMaxWidth(),
                    icons = icons,
                    iconSize = mobileIconSize,
                    isHorizontal = true,
                    onIconClick = {}
                )

                Card(
                    modifier = Modifier.padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        HeaderAndMetricsUi(
                            state,
                            false,
                            onExportClicked = onExportClicked,
                            onDateSelected = { newDate -> viewModel.onDateSelected(newDate) },
                            onSessionSelected = { newSession -> viewModel.onSessionChanged(newSession) }
                        )
                        HandOverlayUi(
                            joints = state.handJoints,
                            onJointClicked = { jointId ->
                                Log.d("HandOverlayUi", "Joint clicked: $jointId")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        GraphsUi(
                            state, modifier = Modifier
                                .height(400.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}