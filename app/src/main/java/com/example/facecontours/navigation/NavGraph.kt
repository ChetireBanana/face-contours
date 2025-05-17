package com.example.facecontours.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.facecontours.presentation.facecontours.FaceContoursContainer
import com.example.facecontours.presentation.noPermission.ErrorScreenContainer
import com.example.facecontours.presentation.onboarding.OnBoardingScreenContainer

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.FaceContouring.route
    ) {
        composable(Screen.FaceContouring.route) {
            FaceContoursContainer(navController)
        }

        composable(Screen.OnBoarding.route) {
            OnBoardingScreenContainer(navController)
        }

        composable(Screen.ErrorScreen.route){
            ErrorScreenContainer(navController)
        }
    }
}