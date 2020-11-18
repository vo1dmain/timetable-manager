package com.vo1d.schedulemanager.v2.ui.instructors.list;

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
import com.vo1d.schedulemanager.v2.data.instructors.Instructor;
import com.vo1d.schedulemanager.v2.data.instructors.InstructorWithJunctions;

import java.util.LinkedList;
import java.util.List;

public class InstructorsListAdapter extends ListAdapter<InstructorWithJunctions, InstructorsListAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<InstructorWithJunctions> DIFF_CALLBACK
            = new DiffUtil.ItemCallback<InstructorWithJunctions>() {
        @Override
        public boolean areItemsTheSame(@NonNull InstructorWithJunctions oldItem,
                                       @NonNull InstructorWithJunctions newItem) {
            return oldItem.instructor.id == newItem.instructor.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull InstructorWithJunctions oldItem,
                                          @NonNull InstructorWithJunctions newItem) {
            return oldItem.equals(newItem);
        }
    };

    private OnItemClickListener itemClickListener;
    private OnSelectionChangedListener selectionChangedListener;

    private SelectionTracker<Long> tracker;

    public InstructorsListAdapter() {
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

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_instructor, parent, false);

        return new ViewHolder(itemView, itemClickListener, selectionChangedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!holder.holderIsSet) {
            setupViewHolder(holder, position);
        }
        if (tracker.hasSelection()) {
            boolean isChecked = tracker.isSelected(getItemId(holder.getAdapterPosition()));
            holder.card.setChecked(isChecked);
            holder.checkBox.setChecked(isChecked);
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).instructor.id;
    }

    public List<Long> getAllIds() {
        List<Long> list = new LinkedList<>();

        for (int i = 0; i < getItemCount(); i++) {
            list.add(getItemId(i));
        }

        return list;
    }

    private void setupViewHolder(@NonNull ViewHolder holder, int position) {
        Instructor current = getItem(position).instructor;

        holder.name.setText(current.getFullName());
        holder.phone.setText(current.phoneNumber);
        holder.firstChar.setText(String.valueOf(current.lastName.charAt(0)));

        holder.holderIsSet = true;
    }

    public interface OnItemClickListener {
        void onItemClick(InstructorWithJunctions instructor);
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged(InstructorWithJunctions instructor, boolean isChecked);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialCardView card;
        private final TextView name;
        private final TextView phone;
        private final TextView firstChar;
        private final CheckBox checkBox;

        private boolean holderIsSet;

        ViewHolder(@NonNull View itemView, final OnItemClickListener cListener, final OnSelectionChangedListener scListener) {
            super(itemView);

            card = (MaterialCardView) itemView;
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            firstChar = itemView.findViewById(R.id.instructor_first_char);
            checkBox = itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (cListener != null && position != RecyclerView.NO_POSITION) {
                    cListener.onItemClick(getItem(position));
                }
            });

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                if (scListener != null && buttonView != null && position != RecyclerView.NO_POSITION) {
                    scListener.onSelectionChanged(getItem(position), isChecked);
                    if (isChecked) {
                        firstChar.setVisibility(View.GONE);
                        tracker.select(InstructorsListAdapter.this.getItemId(position));
                    } else {
                        firstChar.setVisibility(View.VISIBLE);
                        tracker.deselect(InstructorsListAdapter.this.getItemId(position));
                    }
                }
            });
            if (tracker != null) {
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
}
