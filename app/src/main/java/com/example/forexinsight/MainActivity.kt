package com.example.forexinsight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.forexinsight.ui.navigation.BottomBar
import com.example.forexinsight.ui.navigation.NavGraph
import com.example.forexinsight.ui.theme.ForexInsightTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForexInsightTheme(darkTheme = false) {
                val navController = rememberNavController()
                Scaffold(bottomBar = { BottomBar(navController) }) { padding ->
                    Box(Modifier.padding(padding)) {
                        NavGraph(navController)
                    }
                }
            }
        }
    }
}
