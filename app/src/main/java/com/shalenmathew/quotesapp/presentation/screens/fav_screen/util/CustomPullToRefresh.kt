package com.shalenmathew.quotesapp.presentation.screens.fav_screen.util


import androidx.compose.ui.graphics.Shape
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RainbowRays(isRefreshing: Boolean, pullRefreshState: PullToRefreshState) {
    val rayLength by animateFloatAsState(
        targetValue = when {
            pullRefreshState.distanceFraction >= 1f -> 1f
            else -> 0f
        },
        visibilityThreshold = .000001f,
        animationSpec = when {
            pullRefreshState.distanceFraction >= 1f -> tween(2_000, easing = LinearEasing)
            else -> tween(300, easing = LinearEasing)
        }, label = "rayLength"
    )

    val phase = remember { Animatable(0f) }

    LaunchedEffect(pullRefreshState.distanceFraction >= 1f) {
        if (pullRefreshState.distanceFraction >= 1f) {
            var target = 1
            while (true) {
                phase.animateTo(
                    target.toFloat(),
                    animationSpec = tween(1500, easing = LinearEasing)
                )
                target++
            }
        } else {
            phase.animateTo(0f)
        }
    }

    var canvasSize by remember { mutableStateOf(Size.Zero) }

    val rays by remember {
        derivedStateOf {
            val rayMeasure = PathMeasure()
            buildList {
                for (i in 1..7) {
                    val ray = Path()
                    ray.moveTo(canvasSize.center.x, canvasSize.center.y + (5f * i) - 10f)
                    ray.lineTo(canvasSize.width * .8f, canvasSize.center.y + (10f * i) - 20f)
                    ray.relativeLineTo(canvasSize.width * .4f, (100f * (i - 4)))

                    rayMeasure.setPath(ray, false)
                    add(Pair(ray, rayMeasure.length))
                }
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        canvasSize = size
        rays.forEachIndexed { index, (ray, length) ->
            drawPath(
                path = ray,
                color = getRayColor(index),
                style = Stroke(
                    width = 10f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                    pathEffect = PathEffect.chainPathEffect(
                        PathEffect.dashPathEffect(
                            intervals = floatArrayOf(20f, 30f),
                            phase = length * -phase.value
                        ),
                        PathEffect.chainPathEffect(
                            PathEffect.dashPathEffect(
                                intervals = floatArrayOf(length * rayLength, length)
                            ),
                            PathEffect.cornerPathEffect(200f),
                        )
                    )
                )
            )
        }
    }
}


private fun getRayColor(index: Int): Color = when (index) {
    0 -> Color(0xFFFF4D4D)
    1 -> Color(0xFFF07E4A)
    2 -> Color(0xFFFFE354)
    3 -> Color(0xFF7BFD5E)
    4 -> Color(0xFF53E4F7)
    5 -> Color(0xFF5666FC)
    6 -> Color(0xFFDD52F5)
    else -> Color.White
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlowingTriangle(pullRefreshState: PullToRefreshState, isRefreshing: Boolean) {

    val triangleGlow by animateFloatAsState(
        targetValue = when {
            pullRefreshState.distanceFraction > 1f || isRefreshing -> 10f
            else -> 5f
        },
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "triangleGlow",
    )

    Canvas(
        modifier = Modifier
            .clip(TriangleShape)
            .blur(4.dp)
            .fillMaxSize()
    ) {
        val triangle = size.createTrianglePath()
        drawPath(
            path = triangle,
            color = Color.Black,
        )

        drawPath(
            path = triangle,
            color = Color.White,
            style = Stroke(
                width = triangleGlow,
            )
        )
    }

}


//private val TriangleShape = Triangle()
//
//private class Triangle : Shape() {
//    fun createOutline(
//        size: Size,
//        layoutDirection: LayoutDirection,
//        density: Density
//    ): Outline = Outline.Generic(size.createTrianglePath())
//
//    override fun draw(canvas: Canvas?, paint: Paint?) {
//        TODO("Not yet implemented")
//    }
//}

private val TriangleShape = Triangle()

private class Triangle : Shape{
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = Outline.Generic(size.createTrianglePath())

}


private fun Size.createTrianglePath(): Path {
    val triangle = Path()
    triangle.moveTo(center.x - 100f, center.y + 100f)
    triangle.lineTo(center.x, center.y - 100f)
    triangle.lineTo(center.x + 100f, center.y + 100f)
    triangle.close()
    return triangle
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhiteBeam(pullRefreshState: PullToRefreshState, isRefreshing: Boolean) {

    val beamLength by animateFloatAsState(
        targetValue = when {
            isRefreshing -> 1f
            else -> pullRefreshState.distanceFraction
        },
        label = "beamLength",
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val line = Path()
        line.moveTo(0f, size.center.y + 50f)
        line.lineTo(size.center.x, size.center.y)

        val linePathMeasure = PathMeasure()
        linePathMeasure.setPath(line, false)

        drawPath(
            path = line,
            color = Color.White,
            alpha = .06f,
            style = Stroke(
                width = 30f,
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(
                        linePathMeasure.length * beamLength,
                        linePathMeasure.length
                    )
                )
            )
        )

        drawPath(
            path = line,
            color = Color.White,
            style = Stroke(
                width = 5f,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(
                        linePathMeasure.length * beamLength,
                        linePathMeasure.length
                    )
                )
            )
        )
    }

    val beamGlow by animateDpAsState(
        targetValue = when {
            pullRefreshState.distanceFraction > 1f || isRefreshing -> 16.dp
            else -> 2.dp
        },
        label = "beamGlow",
    )

    val beamGlowAlpha by animateFloatAsState(
        targetValue = when {
            pullRefreshState.distanceFraction > 1f || isRefreshing -> .4f
            else -> .1f
        },
        label = "beamGlowAlpha",
    )

    Canvas(
        modifier = Modifier
            .blur(beamGlow, BlurredEdgeTreatment.Unbounded)
            .fillMaxSize()
    ) {
        val line = Path()
        line.moveTo(0f, size.center.y + 50f)
        line.lineTo(size.center.x, size.center.y)

        val linePathMeasure = PathMeasure()
        linePathMeasure.setPath(line, false)

        drawPath(
            path = line,
            color = Color.White,
            alpha = beamGlowAlpha,
            style = Stroke(
                width = 30f,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(
                        linePathMeasure.length * beamLength,
                        linePathMeasure.length
                    )
                )
            )
        )
    }
}
