package com.example.forexinsight.ui.screens.stats

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
fun StatsScreen() {
    val context = LocalContext.current
    val app = context.applicationContext as ForexInsightApp
    val repo = remember { ForexRepository(app.database, context) }
    val stats = remember { StatsUseCase() }
    var info by remember { mutableStateOf("در حال بارگذاری...") }
    LaunchedEffect(Unit) {
        val ds = repo.observeDatasets().first().firstOrNull()
        if (ds == null) { info = "داده‌ای موجود نیست."; return@LaunchedEffect }
        val rates = repo.getRates(ds.id)
        val s = stats.compute(rates)
        val ret = stats.returns(rates)
        val lastRet = ret.lastOrNull()
        info = buildString {
            appendLine("تعداد: ${s.count}")
            appendLine("کمینه: ${s.min}")
            appendLine("بیشینه: ${s.max}")
            appendLine("میانگین: ${s.mean}")
            appendLine("میانه: ${s.median}")
            appendLine("تغییر مطلق: ${s.changeAbs}")
            appendLine("تغییر درصدی: ${s.changePct}")
            appendLine("آخرین بازده ساده: ${lastRet?.simpleReturn}")
            appendLine("آخرین بازده لگاریتمی: ${lastRet?.logReturn}")
            appendLine("این مقادیر تغییر نرخ مرجع هستند، نه سود خالص معامله.")
        }
    }
    Column(Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("آمار و تغییرات", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(info)
    }
}
