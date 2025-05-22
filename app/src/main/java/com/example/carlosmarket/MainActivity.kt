package com.example.carlosmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.carlosmarket.navigate.AppNavigation
import com.example.carlosmarket.ui.theme.CarlosMarketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarlosMarketTheme (darkTheme = true) {
                AppNavigation()
            }
        }
    }
}