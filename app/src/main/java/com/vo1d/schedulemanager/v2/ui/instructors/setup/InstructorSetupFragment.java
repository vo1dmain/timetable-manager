package com.vo1d.schedulemanager.v2.ui.instructors.setup;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.vo1d.schedulemanager.v2.R;
import com.vo1d.schedulemanager.v2.data.instructors.Instructor;
import com.vo1d.schedulemanager.v2.data.instructors.InstructorViewModel;

import java.util.Objects;

public class InstructorSetupFragment extends Fragment {

    private InstructorViewModel ivm;
    private InstructorSetupViewModel isvm;
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
        isvm = provider.get(InstructorSetupViewModel.class);
        ivm = provider.get(InstructorViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instructor_setup, container, false);
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
                isvm.firstNameIsNotEmpty.postValue(!s.toString().trim().isEmpty());
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
                isvm.middleNameIsNotEmpty.postValue(!s.toString().trim().isEmpty());
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
                isvm.lastNameIsNotEmpty.postValue(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        phoneInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher("RU"));

        if (getArguments() != null) {
            int id = getArguments().getInt("instructorId");
            isEditionMode = id != -1;
            if (isEditionMode) {
                isvm.setCurrentInstructor(ivm.findById(id).instructor);
                Instructor current = isvm.getCurrentInstructor();

                firstNameInput.setText(current.firstName);
                middleNameInput.setText(current.middleName);
                lastNameInput.setText(current.lastName);
                phoneInput.setText(current.phoneNumber);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_setup_fragments, menu);

        MenuItem confirm = menu.findItem(R.id.confirm_setup_action);

        isvm.canBeSaved.observe(getViewLifecycleOwner(), confirm::setEnabled);

        confirm.setOnMenuItemClickListener(item -> {
            if (isEditionMode) {
                applyInstructorChanges();
            } else {
                createNewInstructor();
            }

            return Navigation.findNavController(requireView()).navigateUp();
        });
    }

    private void createNewInstructor() {
        String firstName = Objects.requireNonNull(firstNameInput.getText()).toString().trim();
        String middleName = Objects.requireNonNull(middleNameInput.getText()).toString().trim();
        String lastName = Objects.requireNonNull(lastNameInput.getText()).toString().trim();
        String phoneNumber = Objects.requireNonNull(phoneInput.getText()).toString().trim();

        Instructor newInstructor = new Instructor(firstName, middleName, lastName, phoneNumber);

        ivm.insert(newInstructor);
    }

    private void applyInstructorChanges() {
        Instructor current = isvm.getCurrentInstructor();
        current.firstName = Objects.requireNonNull(firstNameInput.getText()).toString().trim();
        current.middleName = Objects.requireNonNull(middleNameInput.getText()).toString().trim();
        current.lastName = Objects.requireNonNull(lastNameInput.getText()).toString().trim();
        current.phoneNumber = Objects.requireNonNull(phoneInput.getText()).toString().trim();

        ivm.update(current);
    }
}
