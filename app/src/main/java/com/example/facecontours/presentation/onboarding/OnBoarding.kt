package com.example.facecontours.presentation.onboarding


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.facecontours.R


@Composable
fun OnBoardingScreenContainer(navController: NavController) {
    Log.d("OnBoarding", "ScreenContainer Init")
    OnBoarding(
        onContinueClicked = {
            navController.navigateUp()
        }
    )
}

@Composable
fun OnBoarding(
    onContinueClicked: () -> Unit
) {
    val context = LocalContext.current
    val image = "android.resource://${context.packageName}/${R.raw.face_contours}".toUri()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(

            model = image,
            contentDescription = "application work example",
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
        )
        Text(
            modifier = Modifier
                .padding(top = 24.dp),
            text = stringResource(R.string.onboarding_screen_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            modifier = Modifier
                .padding(top = 8.dp),
            text = stringResource(R.string.onboarding_screen_text)
        )

        Button(
            modifier = Modifier
                .padding(top = 24.dp),
            onClick = onContinueClicked
        ) {
            Text(text = "Continue")
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun OnBoardingScreenPreview() {
    OnBoarding(onContinueClicked = {})
}
