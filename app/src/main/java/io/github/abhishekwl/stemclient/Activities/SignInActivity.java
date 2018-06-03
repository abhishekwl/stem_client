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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.github.abhishekwl.stemclient.R;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.signInLogoImageView) ImageView logoImageView;
    @BindView(R.id.signInEmailEditText) TextInputEditText emailEditText;
    @BindView(R.id.signInPasswordEditText) TextInputEditText passwordEditText;
    @BindView(R.id.signInProgressBar) ProgressBar progressBar;
    @BindView(R.id.signInButton) Button signInButton;

    private Unbinder unbinder;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentDark));
        setContentView(R.layout.activity_sign_in);

        unbinder = ButterKnife.bind(SignInActivity.this);
        initializeViews();
    }

    private void initializeViews() {
        Glide.with(getApplicationContext()).load(R.drawable.logo).into(logoImageView);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void notifyMessage(String message) {
        Snackbar.make(emailEditText, message, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.signInRegisterTextView)
    public void onSignUpButtonPress() {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        finish();
    }

    @OnClick(R.id.signInButton)
    public void onSignInButtonPress() {
        String clientEmail = emailEditText.getText().toString();
        String clientPassword = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(clientEmail) || TextUtils.isEmpty(clientPassword)) notifyMessage("Email or Password field is empty");
        else signInUser(clientEmail, clientPassword);
    }

    private void signInUser(String clientEmail, String clientPassword) {
        progressBar.setVisibility(View.VISIBLE);
        signInButton.setEnabled(false);

        firebaseAuth.signInWithEmailAndPassword(clientEmail, clientPassword).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            signInButton.setEnabled(true);

            if (task.isSuccessful()) {
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                finish();
            } else notifyMessage(task.getException().getMessage());
        });
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
