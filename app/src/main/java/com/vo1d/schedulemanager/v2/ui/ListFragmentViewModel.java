package com.vo1d.schedulemanager.v2.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vo1d.schedulemanager.v2.data.IMyEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class ListFragmentViewModel<ItemType extends IMyEntity> extends AndroidViewModel {

    protected MutableLiveData<List<ItemType>> selectedItems = new MutableLiveData<>(new LinkedList<>());

    public ListFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public void addToSelection(ItemType item) {
        List<ItemType> list = Objects.requireNonNull(selectedItems.getValue());
        list.add(item);
        selectedItems.postValue(list);
    }

    public void removeFromSelection(ItemType item) {
        List<ItemType> list = Objects.requireNonNull(selectedItems.getValue());
        list.remove(item);
        selectedItems.postValue(list);
    }

    public void clearSelection() {
        Objects.requireNonNull(selectedItems.getValue()).clear();
    }

    public LiveData<List<ItemType>> getSelectedItems() {
        return selectedItems;
    }

    public ItemType[] getSelectedItemsAsArray(@NonNull ItemType[] array) {
        return Objects.requireNonNull(selectedItems.getValue()).toArray(array);
    }
}
