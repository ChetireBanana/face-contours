package com.example.facecontours.navigation

sealed class Screen(val route: String) {
    object OnBoarding : Screen("onBoarding")
    object FaceContouring : Screen("faceContouring")
    object ErrorScreen : Screen("errorScreen")
}