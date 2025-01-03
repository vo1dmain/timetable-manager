package ru.vo1dmain.timetables.settings.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vo1dmain.timetables.design.AppTheme
import ru.vo1dmain.timetables.settings.R

@Composable
internal fun ListPreference(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    entries: Map<String, String>,
    onValueChanged: (String) -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    
    ListItem(
        modifier = modifier.clickable { showDialog = true },
        headlineContent = { Text(title) },
        supportingContent = { Text(entries[value] ?: return@ListItem) }
    )
    
    if (showDialog) {
        ListPreferenceDialog(
            title = { title },
            selected = { value },
            entries = { entries },
            onDismissRequest = { showDialog = false },
            onSelected = onValueChanged
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListPreferenceDialog(
    title: () -> String,
    selected: () -> String,
    entries: () -> Map<String, String>,
    onDismissRequest: () -> Unit = {},
    onSelected: (String) -> Unit = {}
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        content = {
            Surface(
                shape = AlertDialogDefaults.shape,
                color = AlertDialogDefaults.containerColor,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(Modifier.padding(vertical = DialogContainerPadding)) {
                    Text(
                        text = title(),
                        modifier = Modifier.padding(
                            bottom = DialogTitlePadding,
                            start = DialogContainerPadding,
                            end = DialogContainerPadding
                        ),
                        color = AlertDialogDefaults.titleContentColor,
                        style = typography.titleLarge
                    )
                    
                    Column(modifier = Modifier.selectableGroup()) {
                        entries().forEach { current ->
                            val isSelected = current.key == selected()
                            ListItem(
                                colors = ListItemDefaults.colors(
                                    containerColor = AlertDialogDefaults.containerColor
                                ),
                                modifier = Modifier
                                    .padding(horizontal = DialogContainerPadding - DialogTitlePadding)
                                    .selectable(
                                        selected = isSelected,
                                        onClick = {
                                            onSelected(current.key)
                                            onDismissRequest()
                                        },
                                        role = Role.RadioButton
                                    ),
                                headlineContent = { Text(current.value) },
                                leadingContent = { RadioButton(isSelected, null) }
                            )
                        }
                    }
                    TextButton(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(
                                top = DialogContainerPadding,
                                end = DialogContainerPadding,
                                start = DialogContainerPadding
                            ),
                        onClick = onDismissRequest
                    ) {
                        Text(stringResource(R.string.action_title_cancel))
                    }
                }
            }
        }
    )
}

private val DialogTitlePadding = 16.dp
private val DialogContainerPadding = 24.dp

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    AppTheme {
        ListPreference(
            title = "Theme",
            value = "System",
            entries = mapOf(
                "System" to "System",
                "Light" to "Light",
                "Dark" to "Dark"
            )
        ) { }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DialogPreview() {
    AppTheme {
        ListPreferenceDialog(
            { "Theme" },
            { "System" },
            {
                mapOf(
                    "System" to "System",
                    "Light" to "Light",
                    "Dark" to "Dark"
                )
            }
        )
    }
}
