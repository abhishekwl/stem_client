package io.github.abhishekwl.stemclient.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.github.abhishekwl.stemclient.R;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.signUpLogoImageView)
    ImageView logoImageView;
    @BindView(R.id.signUpEmailEditText)
    TextInputEditText emailEditText;
    @BindView(R.id.signUpPasswordEditText) TextInputEditText passwordEditText;
    @BindView(R.id.signUpConfirmPasswordEditText) TextInputEditText confirmPasswordEditText;
    @BindView(R.id.signUpProgressBar)
    ProgressBar signUpProgressBar;

    private Unbinder unbinder;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentDark));
        setContentView(R.layout.activity_sign_up);

        unbinder = ButterKnife.bind(SignUpActivity.this);
        initializeViews();
    }

    private void initializeViews() {
        firebaseAuth = FirebaseAuth.getInstance();
        Glide.with(getApplicationContext()).load(R.drawable.logo).into(logoImageView);
    }

    private void notifyMessage(String message) {
        Snackbar.make(logoImageView, message, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.signUpButton)
    public void onSignUpButtonPress() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) notifyMessage("Please fill all the fields.");
        else if (!password.equals(confirmPassword)) notifyMessage("Passwords do not match.");
        else createNewUser(email, password);
    }

    private void createNewUser(String email, String password) {
        signUpProgressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            signUpProgressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            } else Snackbar.make(logoImageView, task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
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
