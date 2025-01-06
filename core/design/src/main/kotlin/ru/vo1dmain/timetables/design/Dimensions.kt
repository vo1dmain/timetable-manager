package ru.vo1dmain.timetables.design

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class Dimensions(
    val smallSpacerSize: Dp = 8.dp,
    val mediumSpacerSize: Dp = 16.dp,
    val extraLargeImageSize: Dp = 360.dp,
    val largeImageSize: Dp = 240.dp,
    val smallImageSize: Dp = 48.dp,
    val contentPadding: Dp = 16.dp
)

val LocalDimensions = staticCompositionLocalOf { Dimensions() }


val dimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensions.current