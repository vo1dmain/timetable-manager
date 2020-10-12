package com.vo1d.schedulemanager.v2.ui.subjects.setup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.subjects.Subject;
import com.vo1d.schedulemanager.v2.data.subjects.SubjectTypes;
import com.vo1d.schedulemanager.v2.data.subjects.SubjectViewModel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SubjectSetupFragment extends Fragment {

    private SubjectViewModel svm;
    private SubjectSetupViewModel ssvm;
    private TextInputEditText titleInput;
    private TextInputEditText lecturerInput;

    private boolean isEditionMode = false;
    private Subject current;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ViewModelProvider provider = new ViewModelProvider(this);
        ssvm = provider.get(SubjectSetupViewModel.class);
        svm = provider.get(SubjectViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_subject_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleInput = view.findViewById(R.id.subject_title_input);
        lecturerInput = view.findViewById(R.id.subject_lecturer_name_input);
        ChipGroup types = view.findViewById(R.id.types_list);

        List<Integer> chipIds = new LinkedList<>(Collections.emptyList());

        for (SubjectTypes t :
                SubjectTypes.values()) {
            Chip c = (Chip) LayoutInflater.from(requireContext())
                    .inflate(R.layout.chip_subject_type_choice, types, false);
            c.setText(t.toString());
            c.setClickable(true);
            c.setCheckable(true);
            c.setTag(t);
            c.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    ssvm.addToSelection((SubjectTypes) buttonView.getTag());
                } else {
                    ssvm.removeFromSelection((SubjectTypes) buttonView.getTag());
                }
            });
            types.addView(c);

            chipIds.add(c.getId());
        }

        titleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ssvm.titleIsFilled.postValue(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        lecturerInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ssvm.lecturerIsFilled.postValue(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (getArguments() != null) {
            int id = getArguments().getInt("subjectId");
            isEditionMode = id != -1;
            if (isEditionMode) {
                current = svm.findSubjectById(id);
                titleInput.setText(current.title);
                lecturerInput.setText(current.lecturerName);
                for (SubjectTypes t :
                        current.getSubjectTypes()) {
                    types.check(chipIds.get(t.ordinal()));
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        requireActivity().getMenuInflater().inflate(R.menu.menu_setup_fragments, menu);

        MenuItem confirm = menu.findItem(R.id.confirm_setup_action);

        ssvm.canBeSaved.observe(getViewLifecycleOwner(), confirm::setEnabled);

        confirm.setOnMenuItemClickListener(item -> {
            if (isEditionMode) {
                applySubjectChanges();
            } else {
                createNewSubject();
            }

            InputMethodManager imm = requireContext().getSystemService(InputMethodManager.class);
            if (imm != null) {
                imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
            }

            return Navigation.findNavController(requireView()).navigateUp();
        });
    }

    private void createNewSubject() {
        String title = Objects.requireNonNull(titleInput.getText()).toString();
        String lecturer = Objects.requireNonNull(lecturerInput.getText()).toString();

        Subject newSubject = new Subject(title, lecturer, ssvm.getSelectedTypesAsArray());

        svm.insert(newSubject);
    }

    private void applySubjectChanges() {
        current.title = Objects.requireNonNull(titleInput.getText()).toString();
        current.lecturerName = Objects.requireNonNull(lecturerInput.getText()).toString();
        current.setSubjectTypes(ssvm.getSelectedTypesAsArray());

        svm.update(current);
    }
}
