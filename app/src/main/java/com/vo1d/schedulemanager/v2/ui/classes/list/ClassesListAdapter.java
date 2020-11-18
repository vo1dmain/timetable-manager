package com.vo1d.schedulemanager.v2.ui.classes.list;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.vo1d.schedulemanager.v2.MainActivity;
import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.classes.ClassWithCourse;
import com.vo1d.schedulemanager.v2.ui.TimeFormats;
import com.vo1d.schedulemanager.v2.ui.settings.SettingsFragment;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

class ClassesListAdapter extends ListAdapter<ClassWithCourse, ClassesListAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<ClassWithCourse> DIFF_CALLBACK = new DiffUtil.ItemCallback<ClassWithCourse>() {
        @Override
        public boolean areItemsTheSame(@NonNull ClassWithCourse oldItem, @NonNull ClassWithCourse newItem) {
            return oldItem.aClass.id == newItem.aClass.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ClassWithCourse oldItem, @NonNull ClassWithCourse newItem) {
            return oldItem.equals(newItem);
        }
    };
    private final Resources r;
    private final SharedPreferences preferences;
    private final List<EditionModeListener> editionModeListeners = new LinkedList<>();
    private boolean isEditionMode;
    private boolean shouldSelectAll = false;
    private ItemButtonClickListener itemClickListener;
    private SelectionChangedListener selectionChangedListener;

    public ClassesListAdapter(Resources r, SharedPreferences preferences) {
        super(DIFF_CALLBACK);
        setHasStableIds(true);
        this.r = r;
        this.preferences = preferences;
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
        if (!holder.holderIsSet) {
            setupViewHolder(holder, position);
        }
        if (isEditionMode) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.editButton.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(shouldSelectAll);
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

    public void setEditionMode(boolean editionMode) {
        this.isEditionMode = editionMode;
        editionModeListeners.forEach(editionModeListener -> editionModeListener.onEditionModeChanged(editionMode));
    }

    private void setupViewHolder(@NonNull ViewHolder holder, int position) {
        ClassWithCourse current = getItem(position);

        holder.title.setText(current.course.title);

        holder.instructorsChips.removeAllViewsInLayout();
        Chip instructorChip = (Chip) LayoutInflater.from(holder.instructorsChips.getContext())
                .inflate(R.layout.chip_instructor_action, holder.instructorsChips, false);

        String instructor;
        if (current.instructor == null) {
            instructor = "null";
        } else {
            instructor = current.instructor.getShortName();
        }
        instructorChip.setText(instructor);
        holder.instructorsChips.addView(instructorChip);

        holder.typesChips.removeAllViewsInLayout();
        Chip c = (Chip) LayoutInflater.from(holder.typesChips.getContext())
                .inflate(R.layout.chip_course_type_display, holder.typesChips, false);

        String type;
        if (current.aClass.getType() == null) {
            type = "null";
        } else {
            type = current.aClass.getType()[0].toString();
        }
        c.setText(type);
        c.setSelected(true);
        holder.typesChips.addView(c);

        setFormattedTime(current, holder);

        if (current.aClass.audienceBuilding == 0) {
            holder.audience.setText(R.string.audience_distant);
        } else {
            holder.audience.setText(r.getString(R.string.audience_info, current.aClass.getAudienceInfo()));
        }

        holder.holderIsSet = true;
    }

    private void setFormattedTime(ClassWithCourse current, ViewHolder holder) {

        Date startTime = current.aClass.startTime;
        Date endTime = current.aClass.endTime;
        String startTimeString;
        String endTimeString;

        int timeFormatInt = preferences.getInt(SettingsFragment.timeFormatSharedKey, TimeFormats.system.ordinal());

        TimeFormats format = TimeFormats.values()[timeFormatInt];
        switch (format) {
            case system:
                startTimeString = format(startTime,
                        DateFormat.getTimeInstance(Calendar.getInstance(), DateFormat.SHORT)
                );
                endTimeString = format(endTime,
                        DateFormat.getTimeInstance(Calendar.getInstance(), DateFormat.SHORT)
                );
                break;
            case f12Hour:
                startTimeString = format(startTime, MainActivity.f12Hour);
                endTimeString = format(endTime, MainActivity.f12Hour);
                break;
            case f24Hour:
                startTimeString = format(startTime, MainActivity.f24Hour);
                endTimeString = format(endTime, MainActivity.f24Hour);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + format);
        }

        if (startTimeString.contains("AM")) {
            holder.startTimeAM.setVisibility(View.VISIBLE);
            startTimeString = startTimeString.replace(" AM", "");
        } else if (startTimeString.contains("PM")) {
            startTimeString = startTimeString.replace(" PM", "");
            holder.startTimePM.setVisibility(View.VISIBLE);
        }

        if (endTimeString.contains("AM")) {
            holder.endTimeAM.setVisibility(View.VISIBLE);
            endTimeString = endTimeString.replace(" AM", "");
        } else if (endTimeString.contains("PM")) {
            holder.endTimePM.setVisibility(View.VISIBLE);
            endTimeString = endTimeString.replace(" PM", "");
        }

        holder.startTime.setText(startTimeString);
        holder.endTime.setText(endTimeString);
    }

    private String format(Date dateToFormat, DateFormat format) {
        return format.format(dateToFormat);
    }

    private void addEditionModeListener(EditionModeListener listener) {
        this.editionModeListeners.add(listener);
    }

    private void removeEditionModeListener(EditionModeListener listener) {
        this.editionModeListeners.remove(listener);
    }

    public void selectAll() {
        shouldSelectAll = !shouldSelectAll;
        notifyDataSetChanged();
    }

    public interface ItemButtonClickListener {
        void onButtonClick(ClassWithCourse c);
    }

    public interface SelectionChangedListener {
        void onSelectionChanged(ClassWithCourse c, boolean isChecked);
    }

    private interface EditionModeListener {
        void onEditionModeChanged(boolean isEditionMode);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialCardView card;
        private final ChipGroup typesChips;
        private final TextView title;
        private final ChipGroup instructorsChips;
        private final TextView audience;
        private final TextView startTime;
        private final TextView endTime;
        private final TextView startTimeAM;
        private final TextView startTimePM;
        private final TextView endTimeAM;
        private final TextView endTimePM;
        private final CheckBox checkBox;
        private final AppCompatImageButton editButton;

        private final EditionModeListener listener;
        private boolean holderIsSet = false;

        ViewHolder(@NonNull View itemView, final ItemButtonClickListener cListener, final SelectionChangedListener scListener) {
            super(itemView);

            card = (MaterialCardView) itemView;
            typesChips = itemView.findViewById(R.id.course_types);
            title = itemView.findViewById(R.id.course_title);
            instructorsChips = itemView.findViewById(R.id.course_instructors);
            startTime = itemView.findViewById(R.id.class_start_time);
            endTime = itemView.findViewById(R.id.class_end_time);
            audience = itemView.findViewById(R.id.audience_info);
            checkBox = itemView.findViewById(R.id.checkBox);
            editButton = itemView.findViewById(R.id.button_edit);

            CoordinatorLayout startTimeMarkers = itemView.findViewById(R.id.start_time_markers);
            CoordinatorLayout endTimeMarkers = itemView.findViewById(R.id.end_time_markers);

            startTimeAM = startTimeMarkers.findViewById(R.id.time_am);
            startTimePM = startTimeMarkers.findViewById(R.id.time_pm);
            endTimeAM = endTimeMarkers.findViewById(R.id.time_am);
            endTimePM = endTimeMarkers.findViewById(R.id.time_pm);

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