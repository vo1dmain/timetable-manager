package com.vo1d.schedulemanager.v2.ui.courses.list;

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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.courses.CourseTypes;
import com.vo1d.schedulemanager.v2.data.courses.CourseWithInstructors;
import com.vo1d.schedulemanager.v2.data.instructors.InstructorMinimised;

import java.util.LinkedList;
import java.util.List;

class CoursesListAdapter extends ListAdapter<CourseWithInstructors, CoursesListAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<CourseWithInstructors> DIFF_CALLBACK
            = new DiffUtil.ItemCallback<CourseWithInstructors>() {
        @Override
        public boolean areItemsTheSame(@NonNull CourseWithInstructors oldItem,
                                       @NonNull CourseWithInstructors newItem) {
            return oldItem.course.id == newItem.course.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CourseWithInstructors oldItem,
                                          @NonNull CourseWithInstructors newItem) {
            return oldItem.equals(newItem);
        }
    };

    private OnItemClickListener itemClickListener;
    private OnSelectionChangedListener selectionChangedListener;

    private SelectionTracker<Long> tracker;

    public CoursesListAdapter() {
        super(DIFF_CALLBACK);
        setHasStableIds(true);
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

    public List<Long> getAllIds() {
        List<Long> list = new LinkedList<>();

        for (int i = 0; i < getItemCount(); i++) {
            list.add(getItemId(i));
        }

        return list;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_course, parent, false);

        return new ViewHolder(itemView, itemClickListener, selectionChangedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!holder.holderIsSet) {
            setupViewHolder(holder, position);
        }
        if (tracker.hasSelection()) {
            boolean isChecked = tracker.isSelected(getItemId(holder.getAdapterPosition()));
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(isChecked);
            holder.card.setChecked(isChecked);
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).course.id;
    }

    private void setupViewHolder(@NonNull ViewHolder holder, int position) {
        CourseWithInstructors current = getItem(position);

        holder.title.setText(current.course.title);

        holder.instructorsChips.removeAllViewsInLayout();

        for (InstructorMinimised i :
                current.instructors) {
            Chip c = (Chip) LayoutInflater.from(holder.instructorsChips.getContext())
                    .inflate(R.layout.chip_instructor_action, holder.instructorsChips, false);
            c.setText(i.getShortName());
            holder.instructorsChips.addView(c);
        }

        holder.typesChips.removeAllViewsInLayout();

        for (CourseTypes t :
                current.course.getCourseTypes()) {
            Chip c = (Chip) LayoutInflater.from(holder.typesChips.getContext())
                    .inflate(R.layout.chip_course_type_display, holder.typesChips, false);
            c.setText(t.toString());
            c.setSelected(true);
            holder.typesChips.addView(c);
        }

        holder.holderIsSet = true;
    }

    public interface OnItemClickListener {
        void onItemClick(CourseWithInstructors course);
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged(CourseWithInstructors course, boolean isChecked);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialCardView card;
        private final TextView title;
        private final CheckBox checkBox;
        private final ChipGroup typesChips;
        private final ChipGroup instructorsChips;

        private boolean holderIsSet = false;

        ViewHolder(@NonNull View itemView, final OnItemClickListener cListener, final OnSelectionChangedListener scListener) {
            super(itemView);

            card = (MaterialCardView) itemView;
            title = itemView.findViewById(R.id.course_title);
            instructorsChips = itemView.findViewById(R.id.course_instructors);
            checkBox = itemView.findViewById(R.id.checkBox);
            typesChips = itemView.findViewById(R.id.course_types);

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
                        card.setChecked(false);
                        checkBox.setVisibility(View.INVISIBLE);
                    }
                }
            });

        }
    }
}
