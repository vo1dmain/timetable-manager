package com.vo1d.schedulemanager.v2.ui.subjects.list;

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

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.subjects.Subject;
import com.vo1d.schedulemanager.v2.data.subjects.SubjectTypes;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class SubjectsListAdapter extends ListAdapter<Subject, SubjectsListAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<Subject> DIFF_CALLBACK = new DiffUtil.ItemCallback<Subject>() {
        @Override
        public boolean areItemsTheSame(@NonNull Subject oldItem, @NonNull Subject newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Subject oldItem, @NonNull Subject newItem) {
            return oldItem.equals(newItem);
        }
    };

    private OnItemClickListener itemClickListener;
    private OnSelectionChangedListener selectionChangedListener;

    private SelectionTracker<Long> tracker;

    public SubjectsListAdapter() {
        super(DIFF_CALLBACK);
        setHasStableIds(true);
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_subject, parent, false);

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
        Subject current = getItem(position);

        holder.title.setText(current.title);
        holder.lecturer.setText(current.lecturerName);
        holder.typesChips.removeAllViewsInLayout();

        for (SubjectTypes t :
                current.getSubjectTypes()) {
            Chip c = (Chip) LayoutInflater.from(holder.typesChips.getContext())
                    .inflate(R.layout.chip_subject_type_display, holder.typesChips, false);
            c.setText(t.toString());
            c.setSelected(true);
            holder.typesChips.addView(c);
        }

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

    public void removeData(List<Subject> data) {
        List<Subject> list = new LinkedList<>(getCurrentList());

        list.removeAll(data);

        submitList(list);
    }

    public interface OnItemClickListener {
        void onItemClick(Subject subject);
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged(Subject subject, boolean isChecked);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView lecturer;
        private CheckBox checkBox;
        private ChipGroup typesChips;

        ViewHolder(@NonNull View itemView, final OnItemClickListener cListener, final OnSelectionChangedListener scListener) {
            super(itemView);

            title = itemView.findViewById(R.id.subject_title);
            lecturer = itemView.findViewById(R.id.lecturer_name);
            checkBox = itemView.findViewById(R.id.checkBox);
            typesChips = itemView.findViewById(R.id.subject_types_chip_group);

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
