package ru.vo1d.ttmanager.ui.utils

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.selection.SelectionTracker
import ru.vo1d.ttmanager.ActionModeOwner
import ru.vo1d.ttmanager.R

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
        return onCreate?.invoke(mode, menu) == true
    }
    
    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return onPrepare?.invoke(mode, menu) == true
    }
    
    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        if (item.itemId == R.id.action_select_all)
            tracker.setItemsSelected(items, true)
        return onMenuItemClicked?.invoke(mode, item) == true
    }
    
    override fun onDestroyActionMode(mode: ActionMode) {
        owner.actionMode = null
        tracker.clearSelection()
        onDestroy?.invoke(mode)
    }
}
