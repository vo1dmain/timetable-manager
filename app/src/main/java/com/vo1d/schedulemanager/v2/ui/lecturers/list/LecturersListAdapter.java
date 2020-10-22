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

import com.google.android.material.card.MaterialCardView;
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
        holder.phone.setText(current.phoneNumber);
        holder.firstChar.setText(String.valueOf(current.lastName.charAt(0)));

        if (tracker != null) {
            if (tracker.hasSelection()) {
                boolean isChecked = tracker.isSelected(getItemId(holder.getAdapterPosition()));
                holder.card.setChecked(isChecked);
                holder.checkBox.setChecked(isChecked);
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

        private final MaterialCardView card;
        private final TextView name;
        private final TextView phone;
        private final TextView firstChar;
        private final CheckBox checkBox;

        ViewHolder(@NonNull View itemView, final OnItemClickListener cListener, final OnSelectionChangedListener scListener) {
            super(itemView);

            card = (MaterialCardView) itemView;
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
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
                    if (isChecked) {
                        firstChar.setVisibility(View.GONE);
                        tracker.select(LecturersListAdapter.this.getItemId(position));
                    } else {
                        firstChar.setVisibility(View.VISIBLE);
                        tracker.deselect(LecturersListAdapter.this.getItemId(position));
                    }
                }
            });

            tracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
                @Override
                public void onSelectionChanged() {
                    if (!tracker.hasSelection()) {
                        checkBox.setChecked(false);
                        card.setChecked(false);
                        firstChar.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }
}
