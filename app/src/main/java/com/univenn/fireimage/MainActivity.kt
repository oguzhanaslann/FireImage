package com.univenn.fireimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.univenn.fireimage.ui.theme.FireImageTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ImageGenerationViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FireImageTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.ImageGeneration.route,
                    modifier = Modifier
                ) {
                    composable(Screen.ImageGeneration.route) {
                        ImageGenerationScreen(
                            viewModel = viewModel,
                            onChatClicked = { navController.navigate(Screen.Chat.route) },
                            modifier = Modifier
                        )
                    }
                    composable(Screen.Chat.route) {
                        ChatScreen(
                            onBackPressed = { navController.navigateUp() }
                        )
                    }
                }
            }
        }
    }
}

