package io.github.abhishekwl.stemclient.Fragments;


import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import io.github.abhishekwl.stemclient.Helpers.ApiClient;
import io.github.abhishekwl.stemclient.Helpers.ApiInterface;
import io.github.abhishekwl.stemclient.Models.User;
import io.github.abhishekwl.stemclient.R;
import java.util.Objects;
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
    private MaterialDialog materialDialog;
    private static final int RC_PICK_IMAGE = 123;
    private StorageReference storageReference;

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
        storageReference = FirebaseStorage.getInstance().getReference("users/"+firebaseAuth.getUid()+"/profile_picture.jpg");
        initializeBloodGroupSpinner();
        fetchCurrentUser();
    }

    private void fetchCurrentUser() {
        materialDialog = new MaterialDialog.Builder(rootView.getContext())
                .title(R.string.app_name)
                .content("Fetching your profile")
                .progress(true, 0)
                .show();

        if (apiInterface==null) apiInterface = ApiClient.getClient(rootView.getContext()).create(ApiInterface.class);
        apiInterface.getUser(firebaseAuth.getUid()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.body()==null) {
                  if (materialDialog!=null && materialDialog.isShowing()) materialDialog.dismiss();
                  Snackbar.make(profilePictureImageView, "There has been an error fetching your profile :(", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.YELLOW)
                            .setAction("RETRY", v -> {
                                fetchCurrentUser();
                            }).show();
                } else renderUserDetails(Objects.requireNonNull(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
              if (materialDialog!=null && materialDialog.isShowing()) materialDialog.dismiss();
              Snackbar.make(profilePictureImageView, "There has been an error fetching data from the cloud :(", Snackbar.LENGTH_SHORT)
                        .setActionTextColor(colorPrimary)
                        .setAction("RETRY", v -> fetchCurrentUser()).show();
            }
        });
    }

    private void renderUserDetails(User user) {
        if (TextUtils.isEmpty(firebaseAuth.getCurrentUser().getPhotoUrl().toString())) Glide.with(rootView.getContext()).load(R.drawable.logo).into(profilePictureImageView);
        else Glide.with(rootView.getContext()).load(firebaseAuth.getCurrentUser().getPhotoUrl()).into(profilePictureImageView);
        nameEditText.setText(user.getUserName());
        contactNumberEditText.setText(user.getUserContactNumber());
        ageEditText.setText(Integer.toString(user.getUserAge()));
        maleRadioButton.setChecked(user.isUserGender());
        femaleRadioButton.setChecked(!user.isUserGender());
        bloodGroupSpinner.setSelection(stringArrayAdapter.getPosition(user.getUserBloodGroup()));
        medicalHistoryEditText.setText(user.getUserAdditionalInfo());
        if (materialDialog!=null && materialDialog.isShowing()) materialDialog.dismiss();
        Snackbar.make(profilePictureImageView, "Details updated :)", Snackbar.LENGTH_SHORT).show();
    }

    private void initializeBloodGroupSpinner() {
        String[] bloodGroupsArray = rootView.getContext().getResources().getStringArray(R.array.blood_groups);
        stringArrayAdapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_spinner_item, bloodGroupsArray);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(stringArrayAdapter);
        bloodGroupSpinner.setPrompt("Choose Blood Group");
    }

    @OnClick(R.id.profileUpdateProfileButton)
    public void onUpdateButtonPress() {
        materialDialog = new MaterialDialog.Builder(rootView.getContext())
                .title(R.string.app_name)
                .content("Updating")
                .progress(true, 0)
                .show();

        String userImageUrl = (firebaseAuth.getCurrentUser().getPhotoUrl()==null)? "" : firebaseAuth.getCurrentUser().getPhotoUrl().toString();
        String userName = nameEditText.getText().toString();
        String userContactNumber = contactNumberEditText.getText().toString();
        int userAge = Integer.parseInt(ageEditText.getText().toString());
        boolean userGender = maleRadioButton.isChecked();
        String userBloodGroup = bloodGroupSpinner.getSelectedItem().toString();
        String userAdditionalInfo = medicalHistoryEditText.getText().toString();

        if (validation(userName, userContactNumber, userAge, userBloodGroup)) {
          apiInterface.updateUser(firebaseAuth.getUid(), userImageUrl, userName, userContactNumber, userAge, userGender, userBloodGroup, userAdditionalInfo)
              .enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                  if (response.body()==null) {
                    if (materialDialog!=null && materialDialog.isShowing()) materialDialog.dismiss();
                    Snackbar.make(profilePictureImageView, "There has been an error updating your profile :(", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.YELLOW)
                        .setAction("RETRY", v -> {
                          onUpdateButtonPress();
                        }).show();
                  } else renderUserDetails(Objects.requireNonNull(response.body()));
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                  if (materialDialog!=null && materialDialog.isShowing()) materialDialog.dismiss();
                  Snackbar.make(profilePictureImageView, t.getMessage(), Snackbar.LENGTH_LONG)
                      .setActionTextColor(colorPrimary)
                      .setAction("RETRY", v -> {
                        onUpdateButtonPress();
                      }).show();
                }
              });
        } else Snackbar.make(profilePictureImageView, "Please enter valid details", Snackbar.LENGTH_SHORT).show();
    }

  private boolean validation(String userName, String userContactNumber, int userAge, String userBloodGroup) {
    if (TextUtils.isEmpty(userName)) {
      Snackbar.make(profilePictureImageView, "Please enter a valid username", Snackbar.LENGTH_SHORT).show();
      return false;
    } else if (TextUtils.isEmpty(userContactNumber) || !TextUtils.isDigitsOnly(userContactNumber)) {
      Snackbar.make(profilePictureImageView, "Please enter a valid contact number", Snackbar.LENGTH_SHORT).show();
      return false;
    } else if (userAge>0 && userAge<100) {
      Snackbar.make(profilePictureImageView, "Please enter a valid age", Snackbar.LENGTH_SHORT).show();
      return false;
    } else return true;
  }

  @OnClick(R.id.profileImageView)
    public void onProfilePictureImageViewPress() {
      Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
      pickIntent.setType("image/*");
      Intent chooserIntent = Intent.createChooser(pickIntent, "Select Image");
      chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
      startActivityForResult(chooserIntent, RC_PICK_IMAGE);
    }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode==RC_PICK_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null) uploadImageToFirebaseStorage(data.getData());
  }

  private void uploadImageToFirebaseStorage(Uri data) {
    materialDialog = new MaterialDialog.Builder(rootView.getContext())
      .title(R.string.app_name)
      .content("Updating profile picture")
      .progress(true, 0)
      .show();

    storageReference.putFile(data).addOnCompleteListener(task -> {
        if (task.isSuccessful()) updateProfilePictureInFirebaseAuth(task.getResult());
        else {
          if (materialDialog!=null && materialDialog.isShowing()) materialDialog.dismiss();
          Snackbar.make(profilePictureImageView, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    });
  }

  private void updateProfilePictureInFirebaseAuth(TaskSnapshot result) {
    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
      UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
          .setPhotoUri(uri)
          .build();
      firebaseAuth.getCurrentUser().updateProfile(userProfileChangeRequest).addOnFailureListener(
          e -> {
            if (materialDialog!=null && materialDialog.isShowing()) materialDialog.dismiss();
            Snackbar.make(profilePictureImageView, e.getMessage(), Snackbar.LENGTH_SHORT).show();
          }).addOnSuccessListener(aVoid -> {
            if (materialDialog!=null && materialDialog.isShowing()) materialDialog.dismiss();
            Snackbar.make(profilePictureImageView, "Profile picture updated", Snackbar.LENGTH_SHORT).show();
          });
    });
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
