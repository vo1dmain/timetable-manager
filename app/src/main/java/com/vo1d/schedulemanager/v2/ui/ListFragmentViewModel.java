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

public abstract class ListFragmentViewModel<T extends IMyEntity> extends AndroidViewModel {

    protected final MutableLiveData<List<T>> selectedItems = new MutableLiveData<>(new LinkedList<>());

    public ListFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public void addToSelection(T item) {
        List<T> list = Objects.requireNonNull(selectedItems.getValue());
        list.add(item);
        selectedItems.postValue(list);
    }

    public void removeFromSelection(T item) {
        List<T> list = Objects.requireNonNull(selectedItems.getValue());
        list.remove(item);
        selectedItems.postValue(list);
    }

    public void clearSelection() {
        selectedItems.postValue(new LinkedList<>());
    }

    public LiveData<List<T>> getSelectedItems() {
        return selectedItems;
    }

    public T[] getSelectedItemsAsArray(@NonNull T[] array) {
        return Objects.requireNonNull(selectedItems.getValue()).toArray(array);
    }
}
