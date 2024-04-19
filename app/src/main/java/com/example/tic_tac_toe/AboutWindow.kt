package com.example.tic_tac_toe

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AboutPage(){
    Text(
        text = "Reset",
        style = MaterialTheme.typography.headlineSmall,
        color = Color.White,
    )
}