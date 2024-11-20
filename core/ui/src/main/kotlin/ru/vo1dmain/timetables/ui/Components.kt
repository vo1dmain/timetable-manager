package ru.vo1dmain.timetables.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter


@Composable
fun NavigationIcon(onNavigate: () -> Unit) {
    IconButton(onClick = onNavigate) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
    }
}


@Composable
fun SimpleIconToggleButton(
    checked: Boolean,
    checkedIcon: Painter,
    uncheckedIcon: Painter,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {},
    enabled: Boolean = true,
    colors: IconToggleButtonColors = IconButtonDefaults.iconToggleButtonColors(),
    interactionSource: MutableInteractionSource? = null,
    checkedContentDescription: String? = null,
    uncheckedContentDescription: String? = null
) {
    IconToggleButton(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = colors,
        interactionSource = interactionSource
    ) {
        if (checked) {
            Icon(
                painter = checkedIcon,
                contentDescription = checkedContentDescription
            )
        } else {
            Icon(
                painter = uncheckedIcon,
                contentDescription = uncheckedContentDescription
            )
        }
    }
}