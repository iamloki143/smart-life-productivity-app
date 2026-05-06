package com.example.smartlife.ui.screen.home

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.with
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import kotlinx.coroutines.delay
import com.example.smartlife.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val typefaceList = listOf(
        ResourcesCompat.getFont(context, R.font.dancing_script)!!,
        ResourcesCompat.getFont(context, R.font.lobster_two_regular)!!,
        ResourcesCompat.getFont(context, R.font.anton_regular)!!
    )

    var currentFontIndex by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentFontIndex = (currentFontIndex + 1) % typefaceList.size
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = currentFontIndex,
            transitionSpec = {
                fadeIn(animationSpec = tween(800)) with
                        fadeOut(animationSpec = tween(800))
            }
        ) { index ->

            AnimatedStrokeText(
                text = "Focus",
                typeface = typefaceList[index],
                animationKey = index,
                modifier = Modifier.height(120.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Organize your life, track your journey, and build better habits every day.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}