package com.assignment.dashboard.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.assignment.dashboard.ui.theme.BrandLightBlue
import com.assignment.dashboard.ui.theme.MetricBlue

@Composable
fun RomIndicator(angle: Int, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text("ROM", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
            CircularProgressIndicator(
                progress = { angle / 100f },
                modifier = Modifier.fillMaxSize(),
                color = MetricBlue,
                trackColor = BrandLightBlue.copy(alpha = 0.2f),
                strokeWidth = 8.dp
            )
            Text(text = "$angle°", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Gray)
        }
    }
}
