package com.assignment.dashboard.screen

import HistoryPoint
import MovementPoint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.assignment.dashboard.ui.theme.GraphPurple
import com.assignment.dashboard.ui.theme.GridLineGray
import kotlin.collections.forEachIndexed


@OptIn(ExperimentalTextApi::class)
@Composable
fun MovementCanvasGraph(data: List<MovementPoint>, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()

    Column(modifier = modifier) {
        Text("Movement Data", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Canvas(modifier = Modifier.fillMaxSize()) {
            if (data.isEmpty()) return@Canvas

            val paddingBottom = 80f
            // INCREASED LEFT PADDING: To fit the rotated text + the numbers
            val paddingLeft = 100f

            val graphWidth = size.width - paddingLeft
            val graphHeight = size.height - paddingBottom

            val maxY = 60f
            val minY = -20f
            val maxX = 1400f
            val minX = 0f

            val yRange = maxY - minY
            val xRange = maxX - minX

            // Y-Axis Grid Lines & Labels
            val ySteps = listOf(-20, 0, 20, 40, 60)
            ySteps.forEach { step ->
                val yPos = graphHeight - ((step - minY) / yRange * graphHeight)
                drawLine(color = GridLineGray, start = Offset(paddingLeft, yPos), end = Offset(size.width, yPos), strokeWidth = 2f)
                drawText(textMeasurer = textMeasurer, text = step.toString(), topLeft = Offset(paddingLeft - 60f, yPos - 20f))
            }

            // X-Axis Labels
            val xSteps = listOf(0, 200, 400, 600, 800, 1000, 1200, 1400)
            xSteps.forEach { step ->
                val xPos = paddingLeft + ((step - minX) / xRange * graphWidth)
                drawText(textMeasurer = textMeasurer, text = step.toString(), topLeft = Offset(xPos - 20f, graphHeight + 10f))
            }

            // Axis Titles
            drawText(textMeasurer = textMeasurer, text = "Time (s)", topLeft = Offset(paddingLeft + (graphWidth / 2) - 30f, graphHeight + 45f))

            // Rotated Y-Axis Title ("Angle (°)")
            val angleText = "Angle (°)"
            val textLayoutResult = textMeasurer.measure(angleText)
            withTransform({
                rotate(degrees = -90f, pivot = Offset(20f, graphHeight / 2f))
            }) {
                drawText(
                    textLayoutResult = textLayoutResult,
                    color = Color.Black,
                    topLeft = Offset(20f - (textLayoutResult.size.width / 2f), (graphHeight / 2f) - (textLayoutResult.size.height / 2f))
                )
            }

            // Purple Line
            val path = Path()
            data.forEachIndexed { index, point ->
                val x = paddingLeft + ((point.timeSec - minX) / xRange * graphWidth)
                val y = graphHeight - ((point.angle - minY) / yRange * graphHeight)

                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(path = path, color = GraphPurple, style = Stroke(width = 4f))
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun HistoryCanvasGraph(data: List<HistoryPoint>, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {
        if (data.isEmpty()) return@Canvas

        val paddingBottom = 100f
        // INCREASED LEFT PADDING
        val paddingLeft = 100f

        val graphWidth = size.width - paddingLeft
        val graphHeight = size.height - paddingBottom

        val maxY = 50f
        val minY = 0f
        val yRange = maxY - minY

        // 1. Draw Y-Axis Grid & Labels
        val ySteps = listOf(0, 10, 20, 30, 40, 50)
        ySteps.forEach { step ->
            val yPos = graphHeight - ((step - minY) / yRange * graphHeight)
            drawLine(color = GridLineGray, start = Offset(paddingLeft, yPos), end = Offset(size.width, yPos), strokeWidth = 2f)
            drawText(textMeasurer = textMeasurer, text = step.toString(), topLeft = Offset(paddingLeft - 60f, yPos - 20f))
        }

        // NEW: Draw Rotated Y-Axis Title ("ROM (°)")
        val romText = "ROM (°)"
        val textLayoutResult = textMeasurer.measure(romText)
        withTransform({
            rotate(degrees = -90f, pivot = Offset(20f, graphHeight / 2f))
        }) {
            drawText(
                textLayoutResult = textLayoutResult,
                color = Color.Black,
                topLeft = Offset(20f - (textLayoutResult.size.width / 2f), (graphHeight / 2f) - (textLayoutResult.size.height / 2f))
            )
        }

        // 2. Draw Line & Points
        val path = Path()
        val stepX = if (data.size > 1) graphWidth / (data.size - 1) else graphWidth

        data.forEachIndexed { index, point ->
            val x = paddingLeft + (index * stepX)
            val y = graphHeight - ((point.romAngle - minY) / yRange * graphHeight)

            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)

            // Draw X-Axis Multi-line Date Labels
            drawText(textMeasurer = textMeasurer, text = point.date, topLeft = Offset(x - 25f, graphHeight + 20f))
        }

        drawPath(path = path, color = GraphPurple, style = Stroke(width = 4f))

        data.forEachIndexed { index, point ->
            val x = paddingLeft + (index * stepX)
            val y = graphHeight - ((point.romAngle - minY) / yRange * graphHeight)
            drawCircle(color = Color.White, radius = 8f, center = Offset(x, y))
            drawCircle(color = GraphPurple, radius = 8f, center = Offset(x, y), style = Stroke(width = 4f))
        }
    }
}