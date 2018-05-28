package io.github.abhishekwl.stemclient.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.stemclient.R;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splashLogoImageView) ImageView logoImageView;

    private Unbinder unbinder;
    private final int SPLASH_DELAY_LENGTH = 1500;
    private FirebaseAuth firebaseAuth;
    private Intent nextIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        setContentView(R.layout.activity_splash);

        unbinder = ButterKnife.bind(SplashActivity.this);
        initializeViews();
    }

    private void initializeViews() {
        Glide.with(getApplicationContext()).load(R.drawable.logo).into(logoImageView);
        firebaseAuth = FirebaseAuth.getInstance();
        nextIntent = new Intent(SplashActivity.this, MainActivity.class);

        new Handler().postDelayed(() -> {
            if (firebaseAuth.getCurrentUser() == null)
                nextIntent = new Intent(SplashActivity.this, SignInActivity.class);
            startActivity(nextIntent);
            finish();
        }, SPLASH_DELAY_LENGTH);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
