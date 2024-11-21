package ru.vo1dmain.timetables.ui

import android.os.Build
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.DrawableRes
import androidx.appcompat.view.ActionMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.selection.SelectionTracker

fun <T> selectionModeCallback(
    tracker: SelectionTracker<T>,
    owner: ActionModeOwner,
    allItemsProvider: () -> List<T>,
    onCreate: ((ActionMode, Menu) -> Boolean)? = null,
    onPrepare: ((ActionMode, Menu) -> Boolean)? = null,
    onDestroy: ((ActionMode) -> Unit)? = null,
    onMenuItemClicked: ((ActionMode, MenuItem) -> Boolean)? = null
) = object : ActionMode.Callback {
    private lateinit var items: Iterable<T>
    
    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        owner.actionMode = mode
        items = allItemsProvider()
        mode.menuInflater.inflate(R.menu.menu_selection_mode, menu)
        return onCreate?.invoke(mode, menu) ?: false
    }
    
    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return onPrepare?.invoke(mode, menu) ?: false
    }
    
    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        if (item.itemId == R.id.action_select_all)
            tracker.setItemsSelected(items, true)
        return onMenuItemClicked?.invoke(mode, item) ?: false
    }
    
    override fun onDestroyActionMode(mode: ActionMode) {
        owner.actionMode = null
        tracker.clearSelection()
        onDestroy?.invoke(mode)
    }
}

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
