package com.example.tic_tac_toe

import TicTacToe
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.tic_tac_toe.ui.theme.TictactoeTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            MainContent()
        }
    }
    @Composable
    fun MainContent() {
        val context = applicationContext
        val sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

        val isFirstLaunch = remember { sharedPreferences.getBoolean("isFirstLaunch", true) }
        var showPopup by remember { mutableStateOf(isFirstLaunch) }
        var userName by remember { mutableStateOf(TextFieldValue("")) }

        TictactoeTheme {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                SystemBarStyle.light(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.Color.TRANSPARENT
                )
                TicTacToe(sharedPreferences,context)

                if (showPopup) {
                    Dialog(onDismissRequest = {}) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("What is ur name :")
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = userName,
                                onValueChange = { userName = it },
                                label = { Text("name") }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    with(sharedPreferences.edit()) {
                                        putString("userName", userName.text)
                                        putBoolean("isFirstLaunch", false)
                                        apply()
                                    }
                                    showPopup = false
                                }
                            ) {
                                Text("Set name")
                            }
                        }
                    }
                }
            }
        }
    }
}
