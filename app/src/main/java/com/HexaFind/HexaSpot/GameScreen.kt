package com.HexaFind.HexaSpot


import android.os.CountDownTimer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.HexaFind.HexaSpot.ui.theme.HexaSpotTheme
import com.HexaFind.HexaSpot.ui.theme.Primary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun GameScreen(
    level: Int, // current level
    targetScore: Int = level * 100, // target score to proceed to next level
    onBack: () -> Unit,
    onSelect: (Int) -> Unit // function to move to the next level
) {
    var score by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(60) } // timer set to 60 seconds
    val difficulty by remember { mutableFloatStateOf(0.07f * level) } // game difficulty based on level
    val numberOfSpheres = 8 + level * 2 // number of spheres increases with level
    var spheres by remember { mutableStateOf(generateSpheres(numberOfSpheres, difficulty)) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val timer = object : CountDownTimer(60000, 1000) { // 1 minute
            override fun onTick(millisUntilFinished: Long) {
                if (score < targetScore) {
                    timeLeft = (millisUntilFinished / 1000).toInt()
                }
                else {
                    cancel()
                }
            }

            override fun onFinish() {
                timeLeft = 0 // end of game
            }
        }
        timer.start()
    }

    if (timeLeft > 0 && score < targetScore) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.background),
                    contentScale = ContentScale.Crop
                )
                .padding(16.dp),
        ) {
            // Header with timer and score
            IconButton(
                onClick = {
                    onBack.invoke()
                },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(2.dp)
                    .size(52.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .size(38.dp)
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .height(150.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(10.dp))
                    .paint(
                        painterResource(id = R.drawable.header_background),
                        contentScale = ContentScale.FillBounds
                    )
                    .border(
                        width = 0.8.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(10.dp)
                    ),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .padding(vertical = 10.dp)
                ) {
                    AppThemeText(
                        text = "Points: ${score}p",
                        fontSize = 32
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .padding(vertical = 10.dp)
                ) {
                    AppThemeText(
                        text = "Time: ${timeLeft}s",
                        fontSize = 32
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Spheres grid
            GridOfSpheres(
                spheres = spheres,
                onSphereClick = { sphere ->
                    scope.launch {
                        if (sphere.isDifferent) {
                            score += 10 // increase score
                            spheres = spheres.map {
                                if (it.id == sphere.id) {
                                    it.copy(isPopped = true).also {
                                        SoundManager.playSound()
                                    }
                                }
                                else {
                                    it
                                }
                            }
                            delay(800)
                            spheres = spheres.map {
                                if (it.id == sphere.id) {
                                    it.copy(
                                        isDifferent = false,
                                        color = Color.Cyan,
                                        isPopped = false
                                    )
                                }
                                else {
                                    it
                                }
                            }
                            if (spheres.all { !it.isDifferent }) {
                                spheres = generateSpheres(numberOfSpheres, difficulty) // new spheres set
                            }
                        }
                    }
                }
            )
        }
    }
    else {
        val win = score >= targetScore
        // Level complete, proceed to next level
        LaunchedEffect(key1 = win) {
            if (win) Prefs.passLevel(level)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppThemeText(
                text = if (win) "Level\nComplete" else "Try\nAgain",
                modifier = Modifier,
                fontSize = 32
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .height(150.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(10.dp))
                    .paint(
                        painterResource(id = R.drawable.header_background),
                        contentScale = ContentScale.FillBounds
                    )
                    .border(
                        width = 0.8.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(10.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .padding(vertical = 10.dp)
                ) {
                    AppThemeText(
                        text = "Points: ${score}p",
                        fontSize = 32
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .padding(vertical = 10.dp)
                ) {
                    AppThemeText(
                        text = "Time: ${timeLeft}s",
                        fontSize = 32
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            AppThemeButton(
                text = if (win) "Next" else "Again",
                onClick = {
                    if (win) {
                        onSelect.invoke(level + 1)
                    }
                    else {
                        onSelect.invoke(level)
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            AppThemeButton(
                text = "Menu",
                onClick = {
                    onBack.invoke()
                }
            )
        }
    }
}

@Composable
fun AppThemeButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(190.dp)
            .height(48.dp)
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary, // Light blue background to match the provided style
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        HexaSpotTheme {
            AppThemeText(
                text = text,
                fontSize = 24
            )
        }
    }
}
// Function to generate spheres
fun generateSpheres(count: Int, difficulty: Float): List<Sphere> {
    val baseColor = Color.Cyan // base color
    val differentColor = baseColor.copy(
        blue = baseColor.blue * difficulty,
    )

    // Position of spheres that will differ in color
    val differentIndex = List((2 + difficulty * 10).roundToInt()) { Random.nextInt(count) }.toSet()
    return List(count) { index ->
        Sphere(
            color = if (differentIndex.contains(index)) differentColor else baseColor,
            isDifferent = differentIndex.contains(index),
            id = index
        )
    }
}

// Component to display grid of spheres
@Composable
fun GridOfSpheres(spheres: List<Sphere>, onSphereClick: (Sphere) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        val rowCount = sqrt(spheres.size.toFloat()).roundToInt()
        spheres.chunked(rowCount).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { sphere ->
                    SphereView(
                        sphere = sphere,
                        onClick = { onSphereClick(sphere) }
                    )
                }
            }
        }
    }
}

@Composable
fun SphereView(sphere: Sphere, onClick: (Sphere) -> Unit) {
    // Animation for shrinking size and changing transparency
    val scale by animateFloatAsState(
        targetValue = if (sphere.isPopped) 0f else 1f,
        animationSpec = tween(800)
    )

    val hexagonShape = GenericShape { size, _ ->
        val width = size.width
        val height = size.height
        val hexagonWidth = width / 2f
        val hexagonHeight = height / 2f

        moveTo(hexagonWidth, 0f)
        lineTo(width, hexagonHeight / 2f)
        lineTo(width, hexagonHeight + (hexagonHeight / 2f))
        lineTo(hexagonWidth, height)
        lineTo(0f, hexagonHeight + (hexagonHeight / 2f))
        lineTo(0f, hexagonHeight / 2f)
        close()
    }

    AnimatedVisibility(
        visible = scale > 0,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .shadow(elevation = 1.dp, shape = hexagonShape)
                .background(
                    sphere.color.copy(alpha = scale),
                    shape = hexagonShape
                )
                .border(
                    width = 1.dp,
                    color = Color.Black.copy(alpha = scale),
                    shape = hexagonShape
                )
                .clickable {
                    onClick(sphere)
                }
        )
    }
}

// Data model for a sphere
data class Sphere(
    val id: Int,
    val color: Color,
    val isDifferent: Boolean,
    val isPopped: Boolean = false
)