package com.example.forexinsight.ui.screens.trend

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.forexinsight.ForexInsightApp
import com.example.forexinsight.data.repository.ForexRepository
import com.example.forexinsight.domain.usecase.StatsUseCase
import kotlinx.coroutines.flow.first

@Composable
fun TrendScreen() {
    val context = LocalContext.current
    val app = context.applicationContext as ForexInsightApp
    val repo = remember { ForexRepository(app.database, context) }
    val stats = remember { StatsUseCase() }
    var text by remember { mutableStateOf("در حال بارگذاری...") }
    LaunchedEffect(Unit) {
        val ds = repo.observeDatasets().first().firstOrNull()
        if (ds == null) { text = "داده‌ای موجود نیست."; return@LaunchedEffect }
        val rates = repo.getRates(ds.id)
        val sma = stats.sma(rates, 5)
        val lastSma = sma.lastOrNull()?.second
        text = "تحلیل روند نرخ مرجع (نه کندل بازار).\nSMA(5) آخر: ${lastSma ?: "ناموجود"}\nتعداد مشاهدات: ${rates.size}\nRSI استاندارد کندل بازار نمایش داده نمی‌شود."
    }
    Column(Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("تحلیل روند", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(text)
        Spacer(Modifier.height(8.dp))
        Text("INSUFFICIENT_DATA در صورت ناکافی بودن داده.", style = MaterialTheme.typography.bodySmall)
    }
}
