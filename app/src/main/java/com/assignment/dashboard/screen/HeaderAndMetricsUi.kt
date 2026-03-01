package com.assignment.dashboard.screen

import DashboardUiState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.assignment.dashboard.R
import com.assignment.dashboard.ui.theme.ActionRed
import com.assignment.dashboard.ui.theme.BorderGray
import com.assignment.dashboard.ui.theme.MetricBlue
import com.assignment.dashboard.ui.theme.MetricRed
import com.assignment.dashboard.ui.theme.TextDark
import com.assignment.dashboard.ui.theme.TextGray

@Composable
fun HeaderAndMetricsUi(
    state: DashboardUiState, 
    isTablet: Boolean, 
    onExportClicked: () -> Unit,
    onDateSelected: (String) -> Unit = {},
    onSessionSelected: (String) -> Unit = {}
) {
    
    if (isTablet) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            HeaderLeftSection(state, onDateSelected, onSessionSelected)
            HeaderRightSection(state, isMobile = false, onExportClicked = onExportClicked)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            HeaderLeftSection(state, onDateSelected, onSessionSelected)

            HeaderRightSection(state, isMobile = true, onExportClicked = onExportClicked)

        }
    }
}


@Composable
fun HeaderLeftSection(
    state: DashboardUiState,
    onDateSelected: (String) -> Unit,
    onSessionSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        GreetingSection(state.patientName)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DropdownPill(
                currentSelection = state.selectedDate,
                options = state.availableDates,
                onSelectionChanged = onDateSelected
            )
            DropdownPill(
                currentSelection = state.selectedSession,
                options = state.availableSessions,
                onSelectionChanged = onSessionSelected
            )
        }
    }
}

@Composable
fun HeaderRightSection(state: DashboardUiState, isMobile: Boolean, onExportClicked: () -> Unit) {
    val scrollState = rememberScrollState()

    Row(
        modifier = if (isMobile) Modifier.fillMaxWidth() else Modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f, fill = false)
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MetricColumn(label = "Monsoon") {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.LightGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.monsoon),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.clip(CircleShape)
                    )
                }
            }

            MetricColumn(label = "Assissted") {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(MetricRed, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (state.isAssisted) "YES" else "NO",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
            
            BorderedMetric(label = "Session Time", value = state.sessionTimeMins.toString())

            BorderedMetric(label = "Movement Score", value = state.movementScore.toString())

            MetricColumn(label = "Success Rate") {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(64.dp)) {
                    CircularProgressIndicator(
                        progress = { state.successRatePct / 100f },
                        modifier = Modifier.fillMaxSize(),
                        color = MetricBlue,
                        trackColor = Color(0xFFE2E8F0),
                        strokeWidth = 6.dp
                    )
                    Text(
                        text = state.successRatePct.toString(),
                        color = TextDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }

        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Swipe for more details",
                tint = BorderGray,
                modifier = Modifier
                    .size(58.dp)
            )
        }


        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ActionIcons(onExportClicked = onExportClicked)
        }

    }
}

@Composable
fun ActionIcons(onExportClicked: () -> Unit) {
    Icon(
        imageVector = Icons.Default.Delete,
        contentDescription = "Delete",
        tint = ActionRed,
        modifier = Modifier.size(24.dp)
    )
    Icon(
        imageVector = Icons.Default.Refresh,
        contentDescription = "Reload",
        tint = MetricBlue,
        modifier = Modifier.size(24.dp)
    )

    Icon(
        painter = painterResource(R.drawable.ic_export_pdf),
        contentDescription = "Export to PDF",
        tint = MetricBlue,
        modifier = Modifier
            .size(24.dp)
            .clickable { onExportClicked() }
    )
}


@Composable
fun GreetingSection(name: String) {
    Column {
        Text(text = "Hello,", fontSize = 14.sp, color = TextGray)
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = TextDark
            )
            Text(
                text = ", here is your performance.",
                fontSize = 14.sp,
                color = TextDark,
                modifier = Modifier.padding(bottom = 2.dp, start = 4.dp)
            )
        }
    }
}

@Composable
fun DropdownPill(
    currentSelection: String,
    options: List<String>,
    onSelectionChanged: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, TextDark, RoundedCornerShape(20.dp))
                .clickable { expanded = true } // Open menu on click
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentSelection,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TextDark
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Dropdown",
                modifier = Modifier.size(18.dp),
                tint = TextDark
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } 
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelectionChanged(option) 
                        expanded = false           
                    }
                )
            }
        }
    }
}