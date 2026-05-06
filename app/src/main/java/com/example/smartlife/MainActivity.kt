package com.example.smartlife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph
import androidx.navigation.compose.rememberNavController
import com.example.smartlife.navigation.BottomBar
import com.example.smartlife.navigation.NavGraph
import com.example.smartlife.ui.theme.SmartLifeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController= rememberNavController()
            Scaffold(
                bottomBar = {
                    BottomBar(navController)
                }
            ) {padding ->
                Box(modifier = Modifier.padding(padding)){
                    NavGraph(navController)
                }
            }
        }
    }
}

