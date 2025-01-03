package ru.vo1dmain.timetables.settings.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vo1dmain.timetables.design.AppTheme

@Composable
internal fun FlatListPreference(
    modifier: Modifier = Modifier,
    title: String,
    state: State<String>,
    entries: Map<String, String>,
    onValueChanged: (String) -> Unit
) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(top = ContainerTopPadding)
                .selectableGroup()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = TitleHorizontalPadding),
                text = title,
                style = typography.titleMedium
            )
            
            entries.forEach { (key, value) ->
                val isSelected = key == state.value
                ListItem(
                    modifier = Modifier.selectable(
                        selected = isSelected,
                        role = Role.RadioButton,
                        onClick = {
                            onValueChanged(key)
                        }
                    ),
                    headlineContent = { Text(value) },
                    leadingContent = { RadioButton(isSelected, null) }
                )
            }
        }
    }
}

private val TitleHorizontalPadding = 16.dp
private val ContainerTopPadding = 8.dp

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    AppTheme {
        val value = remember { mutableStateOf("System") }
        FlatListPreference(
            title = "Theme",
            state = value,
            entries = mapOf(
                "System" to "System",
                "Light" to "Light",
                "Dark" to "Dark"
            )
        ) {
        }
    }
}