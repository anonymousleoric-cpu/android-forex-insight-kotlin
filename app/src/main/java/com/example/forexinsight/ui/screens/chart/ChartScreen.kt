package com.example.forexinsight.ui.screens.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.forexinsight.ForexInsightApp
import com.example.forexinsight.data.repository.ForexRepository
import com.example.forexinsight.domain.model.ReferenceRate
import kotlinx.coroutines.flow.first

@Composable
fun ChartScreen() {
    val context = LocalContext.current
    val app = context.applicationContext as ForexInsightApp
    val repo = remember { ForexRepository(app.database, context) }
    var rates by remember { mutableStateOf<List<ReferenceRate>>(emptyList()) }
    LaunchedEffect(Unit) {
        val ds = repo.observeDatasets().first().firstOrNull()
        if (ds != null) rates = repo.getRates(ds.id)
    }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("نمودار نرخ مرجع", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        if (rates.isEmpty()) {
            Text("داده‌ای موجود نیست.")
            return
        }
        Text("تعداد مشاهدات: ${rates.size}")
        Spacer(Modifier.height(8.dp))
        LineChart(rates.map { it.rate }, Modifier.fillMaxWidth().height(260.dp))
    }
}

@Composable
fun LineChart(values: List<Double>, modifier: Modifier) {
    Canvas(modifier) {
        if (values.size < 2) return@Canvas
        val min = values.minOrNull() ?: 0.0
        val max = values.maxOrNull() ?: 1.0
        val range = (max - min).coerceAtLeast(1e-9)
        val w = size.width
        val h = size.height
        val step = w / (values.size - 1)
        val path = Path()
        values.forEachIndexed { i, v ->
            val x = i * step
            val y = h - ((v - min) / range * h).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, color = Color(0xFF1976D2), style = Stroke(width = 4f))
        // axes
        drawLine(Color.Gray, Offset(0f, h), Offset(w, h), strokeWidth = 2f)
        drawLine(Color.Gray, Offset(0f, 0f), Offset(0f, h), strokeWidth = 2f)
    }
}
