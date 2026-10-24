package com.example.forexinsight.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    val uriHandler = LocalUriHandler.current
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("تنظیمات و درباره", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        Text("سازنده : دانیال افشاری | تلگرام : SahandSoft")
        Spacer(Modifier.height(8.dp))
        Button(onClick = { uriHandler.openUri("https://t.me/SahandSoft") }) {
            Text("SahandSoft")
        }
        Spacer(Modifier.height(12.dp))
        Text("داده فعلی نرخ مرجع روزانه است، نه کندل کامل بازار.")
        Text("بک‌تست معاملاتی و معیارهای سودآوری تا وارد کردن داده OHLC غیرفعال هستند.")
    }
}
