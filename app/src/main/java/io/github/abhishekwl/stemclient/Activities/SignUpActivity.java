package io.github.abhishekwl.stemclient.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.github.abhishekwl.stemclient.Helpers.ApiClient;
import io.github.abhishekwl.stemclient.Helpers.ApiInterface;
import io.github.abhishekwl.stemclient.Models.User;
import io.github.abhishekwl.stemclient.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.signUpNameEditText) TextInputEditText nameEditText;
    @BindView(R.id.signUpAgeEditText) TextInputEditText ageEditText;
    @BindView(R.id.signUpMaleRadioButton) RadioButton maleRadioButton;
    @BindView(R.id.signUpBloodGroupSpinner) Spinner bloodGroupSpinner;
    @BindView(R.id.signUpContactEditText) TextInputEditText contactNumberEditText;
    @BindView(R.id.signUpEmailEditText) TextInputEditText emailEditText;
    @BindView(R.id.signUpPasswordEditText) TextInputEditText passwordEditText;
    @BindView(R.id.signUpConfirmPasswordEditText) TextInputEditText confirmPasswordEditText;
    @BindView(R.id.signUpButton) Button signUpButton;

    private Unbinder unbinder;
    private FirebaseAuth firebaseAuth;
    private ApiInterface apiInterface;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTabUnselected));
        setContentView(R.layout.activity_sign_up);

        unbinder = ButterKnife.bind(SignUpActivity.this);
        initializeViews();
    }

    private void initializeViews() {
        firebaseAuth = FirebaseAuth.getInstance();
        initializeBloodGroupSpinner();
    }

    private void initializeBloodGroupSpinner() {
        String[] bloodGroupsArray = getResources().getStringArray(R.array.blood_groups);
        ArrayAdapter<String> bloodGroupsArrayAdapter = new ArrayAdapter<>(SignUpActivity.this, android.R.layout.simple_spinner_item, bloodGroupsArray);
        bloodGroupsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(bloodGroupsArrayAdapter);
        bloodGroupSpinner.setPrompt("Choose Blood Group");
    }

    private void notifyMessage(String message) {
        if (materialDialog.isShowing()) materialDialog.dismiss();
        Snackbar.make(signUpButton, message, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.signUpButton)
    public void onSignUpButtonPress() {
        String userName = nameEditText.getText().toString();
        String userAge = ageEditText.getText().toString();
        boolean userGender = maleRadioButton.isSelected();
        String userContactNumber = contactNumberEditText.getText().toString();
        String userBloodGroup = bloodGroupSpinner.getSelectedItem().toString();
        String userEmail = emailEditText.getText().toString();
        String userPassword = passwordEditText.getText().toString();
        String userConfirmPassword = confirmPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(userAge) || TextUtils.isEmpty(userContactNumber) || TextUtils.isEmpty(userBloodGroup)) notifyMessage("Please fill all the fields.");
        else if (!userPassword.equals(userConfirmPassword)) notifyMessage("Passwords do not match.");
        else if (userPassword.length()<8) notifyMessage("Password should contain at least 8 characters.");
        else if (userBloodGroup.equalsIgnoreCase("Choose Blood Group")) notifyMessage("Please choose your blood group.");
        else {
            User newUser = new User(null, userName, Integer.parseInt(userAge), userBloodGroup, userGender, userContactNumber, userEmail);
            createNewUser(newUser, userPassword);
        }
    }

    private void createNewUser(User user, String password) {
        materialDialog = new MaterialDialog.Builder(SignUpActivity.this)
                .title(R.string.app_name)
                .content("Creating Account")
                .progress(true, 0)
                .show();

        firebaseAuth.createUserWithEmailAndPassword(user.getUserEmailId(), password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.setUserId(task.getResult().getUser().getUid());
                pushNewUserDataToDb(user);
            } else notifyMessage(task.getException().getMessage());
        });
    }

    private void pushNewUserDataToDb(User user) {
        if (apiInterface==null) apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
        apiInterface.createNewUser(user.getUserId(), user.getUserName(), user.getUserAge(), user.getUserBloodGroup(), user.isUserGender(), user.getUserContactNumber(), user.getUserEmailId())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        notifyMessage(t.getMessage());
                    }
                });
    }

    @OnClick(R.id.signUpLoginTextView)
    public void onSignInButtonPress() {
        if (firebaseAuth.getCurrentUser()!=null) firebaseAuth.signOut();
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
