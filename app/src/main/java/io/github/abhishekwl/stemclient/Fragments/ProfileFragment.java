package io.github.abhishekwl.stemclient.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.stemclient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.profileImageView) ImageView profilePictureImageView;
    @BindView(R.id.profileNameEditText) TextInputEditText nameEditText;
    @BindView(R.id.profileAgeEditText) TextInputEditText ageEditText;
    @BindView(R.id.profileMaleRadioButton) RadioButton maleRadioButton;
    @BindView(R.id.profileBloodGroupSpinner) Spinner bloodGroupSpinner;
    @BindView(R.id.profileMedicalHistoryEditText) TextInputEditText medicalHistoryEditText;

    private View rootView;
    private Unbinder unbinder;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(ProfileFragment.this, rootView);
        initializeViews();
        return rootView;
    }

    private void initializeViews() {
        Glide.with(rootView.getContext()).load(R.drawable.logo).into(profilePictureImageView);
        initializeBloodGroupSpinner();
    }

    private void initializeBloodGroupSpinner() {
        String[] bloodGroupsArray = rootView.getContext().getResources().getStringArray(R.array.blood_groups);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_spinner_item, bloodGroupsArray);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(stringArrayAdapter);
        bloodGroupSpinner.setPrompt("Choose Blood Group");
    }

    @Override
    public void onStart() {
        super.onStart();
        unbinder = ButterKnife.bind(ProfileFragment.this, rootView);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
