package com.example.facecontours.presentation.noPermission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.facecontours.R
import com.example.facecontours.navigation.Screen

@Composable
fun ErrorScreenContainer(navController: NavController) {
    ErrorScreen {
        navController.navigate(Screen.FaceContouring)
    }
}

@Composable
fun ErrorScreen(
    onTryAgainClicked: () -> Unit
) {

    val context = LocalContext.current
    val image =
        "android.resource://${context.packageName}/${R.raw.impossible_to_run_app_image}".toUri()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp),
            model = image,
            contentDescription = "impossible to run app image",
        )

        Text(
            text = "You can't run this app without camera permission",
        )
        Text(
            text = "Set permissions in settings and try again"
        )

        Button(
            onClick = onTryAgainClicked,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Try again")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    ErrorScreen {

    }
}
