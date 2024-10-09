package com.HexaFind.HexaSpot

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OptionsScreen(
    onBack: () -> Unit
) {
    var musicVolume by remember { mutableFloatStateOf(Prefs.musicVolume) }
    var soundVolume by remember { mutableFloatStateOf(Prefs.soundVolume) }

    // Main background of the screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.background),
                contentScale = ContentScale.Crop
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Content Box
        Box(
            modifier = Modifier
                .width(300.dp)
                .clip(RoundedCornerShape(10.dp))
                .paint(
                    painter = painterResource(id = R.drawable.background_hexagonal),
                    contentScale = ContentScale.FillBounds
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier

                    .padding(16.dp)
            ) {

                AppThemeText(
                    text = "Music",
                    fontSize = 32
                )
                CustomSlider(
                    value = musicVolume,
                    onValueChange = {
                        musicVolume = it
                        Prefs.musicVolume = it
                        SoundManager.setMusicVolume()
                    },
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                // Sound Label and Slider
                AppThemeText(
                    text = "Sound",
                    fontSize = 32
                )

                CustomSlider(
                    value = soundVolume,
                    onValueChange = {
                        soundVolume = it
                        Prefs.soundVolume = it
                        SoundManager.setSoundVolume()
                    },
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "Back Button",
            modifier = Modifier
                .padding(top = 15.dp)
                .size(120.dp, 50.dp)
                .clickable { onBack() }
        )
    }
}

@Composable
fun AppThemeText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: Int = 0
) {
    if (fontSize != 0) {
        Text(
            modifier = modifier,
            text = text,
            fontSize = fontSize.sp,
            style = LocalTextStyle.current.copy(
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(1f, 1f),
                    blurRadius = 2f
                )
            ),
            textAlign = TextAlign.Center
        )
    }
    else {
        Text(
            modifier = modifier,
            text = text,
            style = LocalTextStyle.current.copy(
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(1f, 1f),
                    blurRadius = 2f
                )
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CustomSlider(
    modifier: Modifier = Modifier,
    value: Float = 0.5f,
    onValueChange: (Float) -> Unit = {},
) {
    val trackColor = Color(0xFF99D4FF)
    val thumbColor = Color.Black

    Box(
        modifier = modifier
            .height(50.dp)
            .width(300.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(25.dp)) // rounded shape for the track
            .padding(4.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(25.dp)
            )
            .border(
                width = 2.dp,
                color = thumbColor,
                shape = RoundedCornerShape(25.dp)
            ), // Transparent center to give it border effect
        contentAlignment = Alignment.CenterStart
    ) {
        // Draw the slider track
        if (value != 0f) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .padding(start = 2.dp)
            ) {
                // Draw the background track
                drawRoundRect(
                    color = thumbColor, // border color (maroon)
                    size = Size(size.width * value, size.height), // full size for the border
                    cornerRadius = CornerRadius(100f, 100f),
                    style = Stroke(5f)
                )

                // Draw the inner green track based on slider value
                val trackWidth = size.width * value
                drawRoundRect(
                    color = trackColor, // green track color
                    size = Size(trackWidth, size.height),
                    cornerRadius = CornerRadius(100f, 100f),
                )
            }
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .width(250.dp)
                .alpha(0f),
            colors = SliderDefaults.colors(
                thumbColor = Color.Transparent, // Hide original thumb
                activeTrackColor = Color.Transparent, // Hide original track
                inactiveTrackColor = Color.Transparent
            )
        )
    }

    // Slider control (hidden but for updating value)

}