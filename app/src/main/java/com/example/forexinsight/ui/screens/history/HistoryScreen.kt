package com.example.forexinsight.ui.screens.history

import androidx.compose.foundation.layout.*
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
import kotlinx.coroutines.flow.first

@Composable
fun HistoryScreen() {
    val context = LocalContext.current
    val app = context.applicationContext as ForexInsightApp
    val repo = remember { ForexRepository(app.database, context) }
    var count by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) { count = repo.observeDatasets().first().size }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("تاریخچه تحلیل‌ها", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text("تعداد مجموعه‌های داده: $count")
        Text("تاریخچه تحلیل‌ها پس از اجرای تحلیل‌ها ثبت می‌شود.")
    }
}
