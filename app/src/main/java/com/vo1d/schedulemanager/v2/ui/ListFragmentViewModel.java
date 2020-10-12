package com.vo1d.schedulemanager.v2.ui;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vo1d.schedulemanager.v2.data.IMyEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class ListFragmentViewModel<ItemType extends IMyEntity> extends AndroidViewModel {

    protected LiveData<List<ItemType>> selectedItems = new MutableLiveData<>(new LinkedList<>());
    protected List<View> selectedViews = new LinkedList<>();

    public ListFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public void addToSelection(ItemType item, View itemView) {
        Objects.requireNonNull(selectedItems.getValue()).add(item);
        selectedViews.add(itemView);
    }

    public void removeFromSelection(ItemType item, View itemView) {
        Objects.requireNonNull(selectedItems.getValue()).remove(item);
        selectedViews.remove(itemView);
    }

    public void clearSelection() {
        Objects.requireNonNull(selectedItems.getValue()).clear();
        selectedViews.clear();
    }

    public LiveData<List<ItemType>> getSelectedItems() {
        return selectedItems;
    }

    public ItemType[] getSelectedItemsAsArray(@NonNull ItemType[] array) {
        return Objects.requireNonNull(selectedItems.getValue()).toArray(array);
    }

    public List<View> getSelectedViews() {
        return selectedViews;
    }
}
