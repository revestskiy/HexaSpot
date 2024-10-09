package com.HexaFind.HexaSpot
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.HexaFind.HexaSpot.ui.theme.Primary

@Composable
fun LevelSelectionScreen(onBack: () -> Unit, onSelect: (Int) -> Unit) {
    val scrollState = rememberScrollState()

    // Main background of the screen
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background hexagonal pattern image
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Content Box for levels
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Header text
            AppThemeText(
                text = "Select level:",
                fontSize = 36,
                modifier = Modifier.padding(16.dp)
            )

            // Levels in a grid
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (row in 0 until 5) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val firstLevel = row * 2 + 1
                        LevelButton(level = firstLevel) {
                            SoundManager.playSound()
                            onSelect(firstLevel)
                        }
                        LevelButton(level = firstLevel + 1) {
                            SoundManager.playSound()
                            onSelect(firstLevel + 1)
                        }
                    }
                }
            }
        }

        // Back button
        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "Back Button",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .size(120.dp, 50.dp)
                .clickable {
                    // Handle back button click
                    onBack()
                }
        )
    }
}

@Composable
fun LevelButton(level: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(150.dp, 40.dp)
            .clip(CircleShape)
            .shadow(
                elevation = 2.dp,
                shape = CircleShape
            )
            .background(
                color = Primary,
                shape = CircleShape
            )
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = CircleShape
            )
            .clickable(onClick = onClick)
            .alpha(if (Prefs.levelAvailable(level)) 1f else 0.5f)
    ) {
        // Level text
        Text(
            text = "Level $level",
            fontSize = 24.sp,
            style = LocalTextStyle.current.copy(
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(1f, 1f),
                    blurRadius = 2f
                )
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}