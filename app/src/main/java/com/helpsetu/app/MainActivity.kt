package com.helpsetu.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ui.theme.HelpSetuTheme
import com.helpsetu.app.presentation.navigation.HelpSetuApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelpSetuTheme {
                HelpSetuApp()
            }
        }
    }
}
