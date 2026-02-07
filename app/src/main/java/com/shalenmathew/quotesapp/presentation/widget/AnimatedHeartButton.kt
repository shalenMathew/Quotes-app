package com.shalenmathew.quotesapp.presentation.widget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.shalenmathew.quotesapp.R

@Composable
fun AnimatedHeartButton(
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isAnimating by remember { mutableStateOf(false) }
    var wasLiked by remember { mutableStateOf(isLiked) }

    // Trigger animation when like status changes
    if (wasLiked != isLiked) {
        isAnimating = true
        wasLiked = isLiked
    }

    // Scale animation for the bounce effect
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 1.4f else 1f,
        animationSpec = spring(
            dampingRatio = 0.5f,
            stiffness = 400f
        ),
        finishedListener = { isAnimating = false }
    )

    // Rotation animation for extra flair
    val rotation by animateFloatAsState(
        targetValue = if (isAnimating && isLiked) 15f else 0f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 300f
        )
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clickable {
                isAnimating = true
                onLikeClick()
            }
    ) {
        if (isLiked) {
            AsyncImage(
                model = R.drawable.heart_filled,
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .scale(if (isAnimating) 1.1f else 1f)
                    .rotate(rotation)
            )
        } else {
            AsyncImage(
                model = R.drawable.heart_unfilled,
                contentDescription = "like",
                modifier = Modifier
                    .size(35.dp)
                    .rotate(rotation)
            )
        }
    }
}