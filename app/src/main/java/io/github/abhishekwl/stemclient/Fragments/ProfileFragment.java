package io.github.abhishekwl.stemclient.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.stemclient.Helpers.ApiClient;
import io.github.abhishekwl.stemclient.Helpers.ApiInterface;
import io.github.abhishekwl.stemclient.Models.User;
import io.github.abhishekwl.stemclient.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.profileImageView) ImageView profilePictureImageView;
    @BindView(R.id.profileNameEditText) TextInputEditText nameEditText;
    @BindView(R.id.profileAgeEditText) TextInputEditText ageEditText;
    @BindView(R.id.profileMaleRadioButton) RadioButton maleRadioButton;
    @BindView(R.id.profileFemaleRadioButton) RadioButton femaleRadioButton;
    @BindView(R.id.profileBloodGroupSpinner) Spinner bloodGroupSpinner;
    @BindView(R.id.profileMedicalHistoryEditText) TextInputEditText medicalHistoryEditText;
    @BindView(R.id.profileContactNumberEditText) TextInputEditText contactNumberEditText;
    @BindColor(R.color.colorPrimary) int colorPrimary;

    private View rootView;
    private Unbinder unbinder;
    private ApiInterface apiInterface;
    private FirebaseAuth firebaseAuth;
    private ArrayAdapter<String> stringArrayAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(ProfileFragment.this, rootView);
        initializeViews();
        return rootView;
    }

    private void initializeViews() {
        Glide.with(rootView.getContext()).load(R.drawable.logo).into(profilePictureImageView);
        firebaseAuth = FirebaseAuth.getInstance();
        initializeBloodGroupSpinner();
        fetchCurrentUser();
    }

    private void fetchCurrentUser() {
        if (apiInterface==null) apiInterface = ApiClient.getClient(rootView.getContext()).create(ApiInterface.class);
        apiInterface.getUser(firebaseAuth.getUid()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                User user = response.body();
                renderUserDetails(user);
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.v("USER_FETCH", t.getMessage());
                Snackbar.make(profilePictureImageView, "There has been an error fetching data from the cloud :(", Snackbar.LENGTH_SHORT)
                        .setActionTextColor(colorPrimary)
                        .setAction("ERROR", v -> fetchCurrentUser()).show();
            }
        });
    }

    private void renderUserDetails(User user) {
        if (TextUtils.isEmpty(user.getUserImageUrl())) Glide.with(rootView.getContext()).load(R.drawable.logo).into(profilePictureImageView);
        else Glide.with(rootView.getContext()).load(user.getUserImageUrl()).into(profilePictureImageView);
        nameEditText.setText(user.getUserName());
        contactNumberEditText.setText(user.getUserContactNumber());
        ageEditText.setText(Integer.toString(user.getUserAge()));
        maleRadioButton.setChecked(user.isUserGender());
        femaleRadioButton.setChecked(!user.isUserGender());
        bloodGroupSpinner.setSelection(stringArrayAdapter.getPosition(user.getUserBloodGroup()));
        medicalHistoryEditText.setText(user.getUserAdditionalInfo());

    }

    private void initializeBloodGroupSpinner() {
        String[] bloodGroupsArray = rootView.getContext().getResources().getStringArray(R.array.blood_groups);
        stringArrayAdapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_spinner_item, bloodGroupsArray);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(stringArrayAdapter);
        bloodGroupSpinner.setPrompt("Choose Blood Group");
    }

    private void notifyUser(String message) {
        Snackbar.make(nameEditText, message, Snackbar.LENGTH_SHORT).show();
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
