package ru.vo1dmain.timetables.ui

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap


@Composable
fun mipmapPainterResource(@DrawableRes id: Int): Painter {
    val context = LocalContext.current
    val res = context.resources
    val theme = context.theme
    
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Android O supports adaptive icons, try loading this first (even though this is least likely to be the format).
        val adaptiveIcon = remember(id) {
            ResourcesCompat.getDrawable(res, id, theme)
        }
        if (adaptiveIcon != null) {
            remember(id) {
                BitmapPainter(adaptiveIcon.toBitmap().asImageBitmap())
            }
        } else {
            // We couldn't load the drawable as an Adaptive Icon, just use painterResource
            painterResource(id)
        }
    } else {
        // We're not on Android O or later, just use painterResource
        painterResource(id)
    }
}
