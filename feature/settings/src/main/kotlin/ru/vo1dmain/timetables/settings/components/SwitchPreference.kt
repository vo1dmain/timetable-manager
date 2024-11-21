package ru.vo1dmain.timetables.settings.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.vo1dmain.timetables.design.AppTheme

@Composable
internal fun SwitchPreference(
    modifier: Modifier = Modifier,
    title: String,
    state: State<Boolean>,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        modifier = modifier.clickable {
            onCheckedChange(state.value.not())
        },
        headlineContent = { Text(title) },
        trailingContent = { Switch(checked = state.value, onCheckedChange = null) },
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SwitchPreferencePreview() {
    AppTheme {
        val state = remember { mutableStateOf(false) }
        SwitchPreference(
            title = "Use default time zone",
            state = state,
            onCheckedChange = { }
        )
    }
}