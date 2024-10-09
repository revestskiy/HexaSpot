package com.HexaFind.HexaSpot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.HexaFind.HexaSpot.ui.theme.HexaSpotTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SoundManager.init(application)
        Prefs.init(application)
        setContent {
            HexaSpotTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "loading") {
                    composable("loading") {
                        LoadingScreen {
                            navController.navigate("home") {
                                popUpTo("loading") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                    composable("home") {
                        MainMenuScreen(
                            onOptions = {
                                navController.navigate("options") {
                                    launchSingleTop = true
                                }
                                SoundManager.playSound()
                            },
                            onPlay = {
                                navController.navigate("game") {
                                    launchSingleTop = true
                                }
                                SoundManager.playSound()
                            }
                        )
                    }
                    composable("options") {
                        OptionsScreen {
                            navController.popBackStack()
                            SoundManager.playSound()
                        }
                    }
                    composable("game") {
                        LevelSelectionScreen(
                            onBack = {
                                navController.popBackStack()
                                SoundManager.playSound()
                            },
                            onSelect = { level ->
                                navController.navigate("game/$level") {
                                    launchSingleTop = true
                                }
                                SoundManager.playSound()
                            }
                        )
                    }
                    composable(
                        route = "game/{level}",
                        arguments = listOf(
                            navArgument("level") {
                                type = NavType.IntType
                            }
                        )
                    ) {
                        val level = it.arguments?.getInt("level") ?: 1
                        GameScreen(
                            level = level,
                            onBack = {
                                navController.popBackStack()
                                SoundManager.playSound()
                            },
                            onSelect = {
                                navController.navigate("game/$it") {
                                    launchSingleTop = true
                                    popUpTo("game/$level") { inclusive = true }
                                }
                                SoundManager.playSound()
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        SoundManager.resumeMusic()
    }

    override fun onPause() {
        super.onPause()
        SoundManager.pauseMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundManager.onDestroy()
    }

}

@Composable
fun LoadingScreen(
    onNext: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onNext()

    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.loading),
                contentScale = ContentScale.Crop
            )
    )
}