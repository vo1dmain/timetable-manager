package com.vo1d.schedulemanager.v2.ui.classes.list;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.vo1d.schedulemanager.v2.R;
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
    private final List<EditionModeListener> editionModeListeners = new LinkedList<>();
    private boolean isEditionMode;
    private ItemButtonClickListener itemClickListener;
    private SelectionChangedListener selectionChangedListener;

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

        String s;
        if (current.aClass.getType() == null) {
            s = "null";
        } else {
            s = current.aClass.getType()[0].toString();
        }
        c.setText(s);
        c.setSelected(true);
        holder.typesChips.addView(c);

        holder.startTime.setText(current.aClass.getStartTimeAsString());
        holder.endTime.setText(current.aClass.getEndTimeAsString());

        if (current.aClass.audienceBuilding == 0) {
            holder.audience.setText(R.string.audience_distant);
        } else {
            holder.audience.setText(r.getString(R.string.audience_info, current.aClass.getAudienceInfo()));
        }

        if (isEditionMode) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.editButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).aClass.id;
    }

    public void setItemButtonClickListener(ItemButtonClickListener listener) {
        itemClickListener = listener;
    }

    public void setSelectionChangedListener(SelectionChangedListener listener) {
        selectionChangedListener = listener;
    }

    public void selectAll() {

    }

    public void setEditionMode(boolean editionMode) {
        this.isEditionMode = editionMode;
        editionModeListeners.forEach(editionModeListener -> editionModeListener.onEditionModeChanged(editionMode));
    }

    private void addEditionModeListener(EditionModeListener listener) {
        this.editionModeListeners.add(listener);
    }

    private void removeEditionModeListener(EditionModeListener listener) {
        this.editionModeListeners.remove(listener);
    }

    public interface ItemButtonClickListener {
        void onButtonClick(ClassWithSubject c);
    }

    public interface SelectionChangedListener {
        void onSelectionChanged(ClassWithSubject c, boolean isChecked);
    }

    private interface EditionModeListener {
        void onEditionModeChanged(boolean isEditionMode);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialCardView card;
        private final ChipGroup typesChips;
        private final TextView title;
        private final TextView lecturer;
        private final TextView audience;
        private final TextView startTime;
        private final TextView endTime;
        private final CheckBox checkBox;
        private final AppCompatImageButton editButton;

        private final EditionModeListener listener;

        ViewHolder(@NonNull View itemView, final ItemButtonClickListener cListener, final SelectionChangedListener scListener) {
            super(itemView);

            card = (MaterialCardView) itemView;
            typesChips = itemView.findViewById(R.id.subject_types_chip_group);
            title = itemView.findViewById(R.id.subject_title);
            lecturer = itemView.findViewById(R.id.lecturer_name);
            startTime = itemView.findViewById(R.id.class_start_time);
            endTime = itemView.findViewById(R.id.class_end_time);
            audience = itemView.findViewById(R.id.audience_info);
            checkBox = itemView.findViewById(R.id.checkBox);
            editButton = itemView.findViewById(R.id.button_edit);

            audience.setVisibility(View.VISIBLE);
            checkBox.setClickable(true);

            editButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (cListener != null && position != RecyclerView.NO_POSITION) {
                    cListener.onButtonClick(getItem(position));
                }
            });

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                if (buttonView != null && position != RecyclerView.NO_POSITION) {
                    scListener.onSelectionChanged(getItem(position), isChecked);
                    card.setChecked(isChecked);
                }
            });

            listener = isEditionMode -> {
                if (isEditionMode) {
                    checkBox.setVisibility(View.VISIBLE);
                    editButton.setVisibility(View.VISIBLE);
                    card.setOnClickListener(v -> checkBox.setChecked(!checkBox.isChecked()));

                } else {
                    checkBox.setChecked(false);
                    card.setChecked(false);
                    checkBox.setVisibility(View.INVISIBLE);
                    editButton.setVisibility(View.GONE);
                }
                card.setClickable(isEditionMode);
                card.setCheckable(isEditionMode);
            };

            ClassesListAdapter.this.addEditionModeListener(listener);
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            ClassesListAdapter.this.removeEditionModeListener(listener);
        }
    }
}