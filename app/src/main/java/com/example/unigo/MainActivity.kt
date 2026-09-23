package com.example.unigo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.unigo.navigation.AppNavigation
import com.example.unigo.ui.theme.UnigoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnigoTheme {
                AppNavigation()
            }
        }
    }
}
