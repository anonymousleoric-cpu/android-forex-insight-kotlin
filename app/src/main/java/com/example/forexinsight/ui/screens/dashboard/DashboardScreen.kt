package com.example.forexinsight.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.forexinsight.ForexInsightApp
import com.example.forexinsight.data.repository.ForexRepository
import com.example.forexinsight.ui.screens.dashboard.DashboardViewModel

@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    val app = context.applicationContext as ForexInsightApp
    val repo = remember { ForexRepository(app.database, context) }
    val vm = remember { DashboardViewModel(repo) }
    val state by vm.state.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("داشبورد EUR/USD", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        val ds = state.dataset
        if (ds == null) {
            Text("داده‌ای موجود نیست. لطفاً فایل CSV را وارد کنید.")
            return
        }
        InfoCard("نام دارایی", "EUR/USD")
        InfoCard("نوع داده", ds.dataType.name)
        InfoCard("منبع", ds.source)
        InfoCard("اولین مشاهده", ds.startDate ?: "ناموجود")
        InfoCard("آخرین مشاهده", ds.endDate ?: "ناموجود")
        InfoCard("تعداد مشاهدات", ds.validCount.toString())
        val s = state.stats
        InfoCard("آخرین نرخ", s?.last?.toString() ?: "ناموجود")
        InfoCard("تغییر مطلق", s?.changeAbs?.toString() ?: "ناموجود")
        InfoCard("تغییر درصدی", s?.changePct?.let { "%.4f%%".format(it) } ?: "ناموجود")
        InfoCard("کمترین نرخ", s?.min?.toString() ?: "ناموجود")
        InfoCard("بیشترین نرخ", s?.max?.toString() ?: "ناموجود")
        InfoCard("میانگین", s?.mean?.toString() ?: "ناموجود")
        InfoCard("میانه", s?.median?.toString() ?: "ناموجود")
        Spacer(Modifier.height(8.dp))
        Text("داده فعلی نرخ مرجع روزانه است، نه کندل کامل بازار.", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun InfoCard(label: String, value: String) {
    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, fontWeight = FontWeight.SemiBold)
            Text(value)
        }
    }
}
