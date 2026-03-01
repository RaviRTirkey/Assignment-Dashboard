package com.assignment.dashboard.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.assignment.dashboard.ui.theme.MetricBlue
import com.assignment.dashboard.ui.theme.TextDark

@Composable
fun BorderedMetric(label: String, value: String) {
    MetricColumn(label = label) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .border(6.dp, MetricBlue, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                color = TextDark,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun MetricColumn(label: String, content: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = TextDark
        )
        content()
    }
}