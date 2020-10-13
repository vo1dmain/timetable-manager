package com.vo1d.schedulemanager.v2.ui.lecturers.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.lecturers.Lecturer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class LecturersListAdapter extends ListAdapter<Lecturer, LecturersListAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<Lecturer> DIFF_CALLBACK = new DiffUtil.ItemCallback<Lecturer>() {
        @Override
        public boolean areItemsTheSame(@NonNull Lecturer oldItem, @NonNull Lecturer newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Lecturer oldItem, @NonNull Lecturer newItem) {
            return oldItem.equals(newItem);
        }
    };

    private OnItemClickListener itemClickListener;
    private OnSelectionChangedListener selectionChangedListener;

    private SelectionTracker<Long> tracker;

    public LecturersListAdapter() {
        super(DIFF_CALLBACK);
        setHasStableIds(true);
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_lecturer, parent, false);

        return new ViewHolder(itemView, itemClickListener, selectionChangedListener);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        selectionChangedListener = listener;
    }

    public void setTracker(SelectionTracker<Long> tracker) {
        this.tracker = tracker;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lecturer current = getItem(position);

        holder.name.setText(current.getFullName());
        holder.firstChar.setText(String.valueOf(current.firstName.charAt(0)));

        if (tracker != null) {
            if (tracker.hasSelection()) {
                holder.checkBox.setChecked(tracker.isSelected(getItemId(holder.getAdapterPosition())));
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    public List<Long> getAllIds() {
        List<Long> list = new LinkedList<>();

        for (int i = 0; i < getItemCount(); i++) {
            list.add(getItemId(i));
        }

        return list;
    }

    public void clearData() {
        submitList(Collections.emptyList());
    }

    public void removeData(List<Lecturer> data) {
        List<Lecturer> list = new LinkedList<>(getCurrentList());

        list.removeAll(data);

        submitList(list);
    }

    public interface OnItemClickListener {
        void onItemClick(Lecturer lecturer);
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged(Lecturer lecturer, boolean isChecked);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView firstChar;
        private CheckBox checkBox;

        ViewHolder(@NonNull View itemView, final OnItemClickListener cListener, final OnSelectionChangedListener scListener) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            firstChar = itemView.findViewById(R.id.lecturer_first_char);
            checkBox = itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (cListener != null && position != RecyclerView.NO_POSITION) {
                    cListener.onItemClick(getItem(position));
                }
            });

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                if (buttonView != null && position != RecyclerView.NO_POSITION) {
                    scListener.onSelectionChanged(getItem(position), isChecked);
                }
            });

            tracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
                @Override
                public void onSelectionChanged() {
                    if (tracker.hasSelection()) {
                        checkBox.setVisibility(View.VISIBLE);
                    } else if (!tracker.hasSelection()) {
                        checkBox.setChecked(false);
                        checkBox.setVisibility(View.INVISIBLE);
                    }
                }
            });

        }
    }
}
