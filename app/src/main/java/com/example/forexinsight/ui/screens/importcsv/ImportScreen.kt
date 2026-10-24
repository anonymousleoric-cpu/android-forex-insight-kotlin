package com.example.forexinsight.ui.screens.importcsv

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.forexinsight.ForexInsightApp
import com.example.forexinsight.data.repository.ForexRepository

@Composable
fun ImportScreen() {
    val context = LocalContext.current
    val app = context.applicationContext as ForexInsightApp
    val repo = remember { ForexRepository(app.database, context) }
    val vm = remember { ImportViewModel(repo) }
    val state by vm.state.collectAsState()
    val pick = rememberCsvPicker { uri, name -> vm.import(uri, name) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("وارد کردن CSV", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        Button(onClick = pick, enabled = !state.loading) { Text("انتخاب فایل CSV") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { vm.clear() }) { Text("حذف همه داده‌ها") }
        Spacer(Modifier.height(12.dp))
        if (state.loading) CircularProgressIndicator()
        state.report?.let { r ->
            Text("معتبر: ${r.valid}")
            Text("ردشده: ${r.rejected}")
            Text("تکراری: ${r.duplicates}")
            if (r.errors.isNotEmpty()) {
                Text("خطاها:", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                r.errors.forEach { Text(it, style = MaterialTheme.typography.bodySmall) }
            }
        }
        state.error?.let { Text("خطا: $it", color = MaterialTheme.colorScheme.error) }
    }
}
