package ru.vo1dmain.timetables.ui

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
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