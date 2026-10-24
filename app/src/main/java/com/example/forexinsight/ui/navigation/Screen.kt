package com.example.forexinsight.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Dashboard : Screen("dashboard", "داشبورد")
    object Chart : Screen("chart", "نمودار نرخ")
    object Stats : Screen("stats", "آمار و تغییرات")
    object Import : Screen("import", "وارد کردن CSV")
    object Trend : Screen("trend", "تحلیل روند")
    object Backtest : Screen("backtest", "بک‌تست")
    object History : Screen("history", "تاریخچه تحلیل‌ها")
    object Settings : Screen("settings", "تنظیمات")
}
