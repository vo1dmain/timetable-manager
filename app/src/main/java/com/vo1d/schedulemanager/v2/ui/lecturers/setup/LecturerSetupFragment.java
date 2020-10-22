package com.vo1d.schedulemanager.v2.ui.lecturers.setup;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
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

import com.google.android.material.textfield.TextInputEditText;
import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.lecturers.Lecturer;
import com.vo1d.schedulemanager.v2.data.lecturers.LecturerViewModel;

import java.util.Objects;

public class LecturerSetupFragment extends Fragment {

    private LecturerViewModel lvm;
    private LecturerSetupViewModel lsvm;
    private TextInputEditText firstNameInput;
    private TextInputEditText middleNameInput;
    private TextInputEditText lastNameInput;
    private TextInputEditText phoneInput;

    private boolean isEditionMode = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ViewModelProvider provider = new ViewModelProvider(this);
        lsvm = provider.get(LecturerSetupViewModel.class);
        lvm = provider.get(LecturerViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_lecturer_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstNameInput = view.findViewById(R.id.first_name_input);
        middleNameInput = view.findViewById(R.id.middle_name_input);
        lastNameInput = view.findViewById(R.id.last_name_input);
        phoneInput = view.findViewById(R.id.phone_input);

        firstNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lsvm.firstNameIsNotEmpty.postValue(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        middleNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lsvm.middleNameIsNotEmpty.postValue(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        lastNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lsvm.lastNameIsNotEmpty.postValue(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        phoneInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher("RU"));

        if (getArguments() != null) {
            int id = getArguments().getInt("lecturerId");
            isEditionMode = id != -1;
            if (isEditionMode) {
                lsvm.setCurrentLecturer(lvm.findLecturerById(id));
                firstNameInput.setText(lsvm.getCurrentLecturer().firstName);
                middleNameInput.setText(lsvm.getCurrentLecturer().middleName);
                lastNameInput.setText(lsvm.getCurrentLecturer().lastName);
                phoneInput.setText(lsvm.getCurrentLecturer().phoneNumber);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        requireActivity().getMenuInflater().inflate(R.menu.menu_setup_fragments, menu);

        MenuItem confirm = menu.findItem(R.id.confirm_setup_action);

        lsvm.canBeSaved.observe(getViewLifecycleOwner(), confirm::setEnabled);

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
        String firstName = Objects.requireNonNull(firstNameInput.getText()).toString();
        String middleName = Objects.requireNonNull(middleNameInput.getText()).toString();
        String lastName = Objects.requireNonNull(lastNameInput.getText()).toString();
        String phoneNumber = Objects.requireNonNull(phoneInput.getText()).toString();

        Lecturer newLecturer = new Lecturer(firstName, middleName, lastName, phoneNumber);

        lvm.insert(newLecturer);
    }

    private void applySubjectChanges() {
        Lecturer current = lsvm.getCurrentLecturer();
        current.firstName = Objects.requireNonNull(firstNameInput.getText()).toString();
        current.middleName = Objects.requireNonNull(middleNameInput.getText()).toString();
        current.lastName = Objects.requireNonNull(lastNameInput.getText()).toString();
        current.phoneNumber = Objects.requireNonNull(phoneInput.getText()).toString();

        lvm.update(current);
    }
}
