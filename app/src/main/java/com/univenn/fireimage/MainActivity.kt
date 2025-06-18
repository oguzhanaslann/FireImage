package com.univenn.fireimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.univenn.fireimage.navigation.Screen
import com.univenn.fireimage.screens.chat.ChatScreen
import com.univenn.fireimage.screens.image_generation.ImageGenerationScreen
import com.univenn.fireimage.screens.image_generation.ImageGenerationViewModel
import com.univenn.fireimage.ui.theme.FireImageTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ImageGenerationViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FireImageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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
}

