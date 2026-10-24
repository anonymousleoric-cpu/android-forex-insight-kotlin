package com.example.forexinsight.ui.screens.backtest

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BacktestScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("بک‌تست", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        Text("بک‌تست معاملاتی غیرفعال است: داده فعلی نرخ مرجع است، نه کندل OHLC.")
        Spacer(Modifier.height(8.dp))
        Text("NOT_TESTED")
        Spacer(Modifier.height(8.dp))
        Text("INSUFFICIENT_DATA برای اجرای بک‌تست معاملاتی.")
    }
}
