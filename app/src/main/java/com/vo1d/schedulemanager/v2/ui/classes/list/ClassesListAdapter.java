package com.vo1d.schedulemanager.v2.ui.classes.list;

import android.content.res.Resources;
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
import com.vo1d.schedulemanager.v2.data.classes.Class;
import com.vo1d.schedulemanager.v2.data.classes.ClassWithSubject;

import java.util.LinkedList;
import java.util.List;

class ClassesListAdapter extends ListAdapter<ClassWithSubject, ClassesListAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<ClassWithSubject> DIFF_CALLBACK = new DiffUtil.ItemCallback<ClassWithSubject>() {
        @Override
        public boolean areItemsTheSame(@NonNull ClassWithSubject oldItem, @NonNull ClassWithSubject newItem) {
            return oldItem.aClass.id == newItem.aClass.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ClassWithSubject oldItem, @NonNull ClassWithSubject newItem) {
            return oldItem.equals(newItem);
        }
    };
    private final Resources r;

    private OnItemClickListener itemClickListener;
    private OnSelectionChangedListener selectionChangedListener;

    private SelectionTracker<Long> tracker;

    public ClassesListAdapter(Resources r) {
        super(DIFF_CALLBACK);
        setHasStableIds(true);
        this.r = r;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_class, parent, false);

        return new ViewHolder(itemView, itemClickListener, selectionChangedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ClassWithSubject current = getItem(position);

        holder.title.setText(current.subject.title);
        holder.lecturer.setText(current.subject.lecturerName);

        holder.typesChips.removeAllViewsInLayout();
        Chip c = (Chip) LayoutInflater.from(holder.typesChips.getContext())
                .inflate(R.layout.chip_subject_type_display, holder.typesChips, false);
        c.setText(current.aClass.getType()[0].toString());
        c.setSelected(true);
        holder.typesChips.addView(c);

        holder.startTime.setText(current.aClass.getStartTimeAsString());
        holder.endTime.setText(current.aClass.getEndTimeAsString());

        if (current.aClass.audienceBuilding == 0) {
            holder.audience.setText(R.string.audience_distant);
        } else {
            holder.audience.setText(r.getString(R.string.audience_info, current.aClass.getAudienceInfo()));
        }

        if (tracker != null) {
            if (tracker.hasSelection()) {
                holder.checkBox.setChecked(tracker.isSelected(getItemId(holder.getAdapterPosition())));
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).aClass.id;
    }

    public List<Long> getAllIds() {
        List<Long> list = new LinkedList<>();

        for (int i = 0; i < getItemCount(); i++) {
            list.add(getItemId(i));
        }

        return list;
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

    public interface OnItemClickListener {
        void onItemClick(Class c);
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged(Class c, View parent, boolean isChecked);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ChipGroup typesChips;
        private TextView title;
        private TextView lecturer;
        private TextView audience;
        private TextView startTime;
        private TextView endTime;
        private CheckBox checkBox;

        ViewHolder(@NonNull View itemView, final OnItemClickListener cListener, final OnSelectionChangedListener scListener) {
            super(itemView);

            typesChips = itemView.findViewById(R.id.subject_types_chip_group);
            title = itemView.findViewById(R.id.subject_title);
            lecturer = itemView.findViewById(R.id.lecturer_name);
            startTime = itemView.findViewById(R.id.class_start_time);
            endTime = itemView.findViewById(R.id.class_end_time);
            audience = itemView.findViewById(R.id.audience_info);

            audience.setVisibility(View.VISIBLE);

            checkBox = itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (cListener != null && position != RecyclerView.NO_POSITION) {
                    cListener.onItemClick(getItem(position).aClass);
                }
            });

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                if (buttonView != null && position != RecyclerView.NO_POSITION) {
                    scListener.onSelectionChanged(getItem(position).aClass, itemView, isChecked);
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