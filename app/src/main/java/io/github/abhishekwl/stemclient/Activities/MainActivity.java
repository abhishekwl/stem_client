package io.github.abhishekwl.stemclient.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.icu.util.Currency;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.google.firebase.auth.FirebaseAuth;
import io.github.abhishekwl.stemclient.Adapters.MainViewPagerAdapter;
import io.github.abhishekwl.stemclient.R;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainTabLayout) TabLayout tabLayout;
    @BindView(R.id.mainViewPager) ViewPager viewPager;
    @BindColor(R.color.colorTabUnselected) int colorTabUnselected;
    @BindColor(android.R.color.white) int colorWhite;
    @BindColor(R.color.colorPrimary) int colorPrimary;
    @BindColor(R.color.colorTextLight) int colorTextLight;
    @BindColor(R.color.colorTextDark) int colorTextDark;

    private Unbinder unbinder;
    public static Currency currency;
    public static Locale deviceLocale;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(MainActivity.this);
        initializeViews();
    }

    private void initializeViews() {
        deviceLocale = Locale.getDefault();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            currency = Currency.getInstance(deviceLocale);
        initializeTabLayoutAndViewPager();
        initializeFirebase();
    }

    private void initializeFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() == null) {
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
            }
        });
    }

    private void initializeTabLayoutAndViewPager() {
        Objects.requireNonNull(getSupportActionBar()).setElevation(0f);
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) tabLayout.setElevation(8f);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_home_black_24dp);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_search_black_24dp);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.ic_history_black_24dp);
        Objects.requireNonNull(tabLayout.getTabAt(3)).setIcon(R.drawable.ic_person_black_24dp);
        Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(0)).getIcon()).setColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
        Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(1)).getIcon()).setColorFilter(colorTextLight, PorterDuff.Mode.SRC_IN);
        Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(2)).getIcon()).setColorFilter(colorTextLight, PorterDuff.Mode.SRC_IN);
        Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(3)).getIcon()).setColorFilter(colorTextLight, PorterDuff.Mode.SRC_IN);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Objects.requireNonNull(tab.getIcon()).setColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Objects.requireNonNull(tab.getIcon()).setColorFilter(colorTextLight, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemLogout:
                firebaseAuth.signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
