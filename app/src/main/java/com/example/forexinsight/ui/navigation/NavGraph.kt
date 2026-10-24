package com.example.forexinsight.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.forexinsight.ui.screens.backtest.BacktestScreen
import com.example.forexinsight.ui.screens.chart.ChartScreen
import com.example.forexinsight.ui.screens.dashboard.DashboardScreen
import com.example.forexinsight.ui.screens.history.HistoryScreen
import com.example.forexinsight.ui.screens.importcsv.ImportScreen
import com.example.forexinsight.ui.screens.settings.SettingsScreen
import com.example.forexinsight.ui.screens.stats.StatsScreen
import com.example.forexinsight.ui.screens.trend.TrendScreen

val bottomItems = listOf(
    Screen.Dashboard, Screen.Chart, Screen.Stats, Screen.Import, Screen.Trend,
    Screen.Backtest, Screen.History, Screen.Settings
)

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
        composable(Screen.Dashboard.route) { DashboardScreen() }
        composable(Screen.Chart.route) { ChartScreen() }
        composable(Screen.Stats.route) { StatsScreen() }
        composable(Screen.Import.route) { ImportScreen() }
        composable(Screen.Trend.route) { TrendScreen() }
        composable(Screen.Backtest.route) { BacktestScreen() }
        composable(Screen.History.route) { HistoryScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar {
        bottomItems.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(screen.title) },
                icon = { Icon(Icons.Filled.Info, contentDescription = screen.title) }
            )
        }
    }
}
