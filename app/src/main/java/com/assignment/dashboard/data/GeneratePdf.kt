package com.assignment.dashboard.data

import DashboardUiState
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast

fun exportDataToPdf(context: Context, uri: Uri, state: DashboardUiState) {
    try {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val titlePaint = Paint().apply {
            textSize = 24f
            isFakeBoldText = true
            color = android.graphics.Color.BLACK
        }
        val normalPaint = Paint().apply {
            textSize = 14f
            color = android.graphics.Color.DKGRAY
        }

        canvas.drawText("Patient Performance Report", 50f, 60f, titlePaint)

        canvas.drawText("Name: ${state.patientName}", 50f, 110f, normalPaint)
        canvas.drawText("Date: ${state.selectedDate}", 50f, 140f, normalPaint)
        canvas.drawText("Session: ${state.selectedSession}", 50f, 170f, normalPaint)

        canvas.drawText("Assisted: ${if (state.isAssisted) "YES" else "NO"}", 350f, 110f, normalPaint)
        canvas.drawText("Session Time: ${state.sessionTimeMins} mins", 350f, 140f, normalPaint)
        canvas.drawText("Movement Score: ${state.movementScore}", 350f, 170f, normalPaint)
        canvas.drawText("Success Rate: ${state.successRatePct}%", 350f, 200f, normalPaint)

        val linePaint = Paint().apply {
            color = android.graphics.Color.LTGRAY
            strokeWidth = 2f
        }
        canvas.drawLine(50f, 230f, 545f, 230f, linePaint)

        canvas.drawText("Movement Data Points Recorded: ${state.movementData.size}", 50f, 270f, normalPaint)
        canvas.drawText("History Data Points Recorded: ${state.historyData.size}", 50f, 300f, normalPaint)
        
        pdfDocument.finishPage(page)

        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            pdfDocument.writeTo(outputStream)
        }

        pdfDocument.close()
        Toast.makeText(context, "PDF Saved Successfully!", Toast.LENGTH_SHORT).show()

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to save PDF: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}