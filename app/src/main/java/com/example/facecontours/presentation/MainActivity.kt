package com.example.facecontours.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.facecontours.navigation.NavGraph
import com.example.facecontours.ui.theme.FaceContoursTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate")

        setContent {
            FaceContoursTheme {
                val navController = rememberNavController()
                NavGraph(navController)

            }

        }
    }
}