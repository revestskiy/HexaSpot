package com.HexaFind.HexaSpot

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun MainMenuScreen(
    onPlay: () -> Unit = {},
    onOptions: () -> Unit = {},
) {
    val context = LocalContext.current as Activity
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.background), // используйте свой фон
                contentScale = ContentScale.Crop
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f)) // Пустое пространство сверху

            // Верхняя часть: Play Button
            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.play),
                contentDescription = "Play Button",
                modifier = Modifier
                    .size(220.dp, 70.dp)
                    .clickable {
                        onPlay()
                        // Действие при нажатии
                    }
            )

            Spacer(modifier = Modifier.height(16.dp)) // Расстояние между Play и Options

            // Средняя часть: Options Button
            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.options),
                contentDescription = "Options Button",
                modifier = Modifier
                    .size(220.dp, 70.dp)
                    .clickable { // Действие при нажатии
                        onOptions()
                    }
            )

            Spacer(modifier = Modifier.weight(1f)) // Пустое пространство снизу для центрирования Play и Options

            // Нижняя часть: Exit Button
            Image(
                bitmap = ImageBitmap.imageResource(id = R.drawable.exit),
                contentDescription = "Exit Button",
                modifier = Modifier
                    .size(160.dp, 60.dp)
                    .clickable {
                        context.finishAndRemoveTask()
                    }
            )
            Spacer(modifier = Modifier.weight(1f))

        }
    }
}
