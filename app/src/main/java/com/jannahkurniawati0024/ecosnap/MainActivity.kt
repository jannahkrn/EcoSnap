package com.jannahkurniawati0024.ecosnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.jannahkurniawati0024.ecosnap.navigation.NavGraph
import com.jannahkurniawati0024.ecosnap.ui.theme.EcoSnapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoSnapTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavGraph()
                }
            }
        }
    }
}