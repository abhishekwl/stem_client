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

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.stemclient.Adapters.MainViewPagerAdapter;
import io.github.abhishekwl.stemclient.Fragments.TestFragment;
import io.github.abhishekwl.stemclient.R;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainTabLayout) TabLayout tabLayout;
    @BindView(R.id.mainViewPager) ViewPager viewPager;
    public static Locale deviceLocale;
    @BindColor(R.color.colorAccentDark) int colorAccentDark;
    @BindColor(R.color.colorTabUnselected) int colorTabUnselected;
    @BindColor(android.R.color.white) int colorWhite;

    private Unbinder unbinder;
    private FirebaseAuth firebaseAuth;
    private TestFragment testFragment;
    private MainViewPagerAdapter mainViewPagerAdapter;
    public static Currency currency;
    @BindView(R.id.mainToolbar)
    android.support.v7.widget.Toolbar mainToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(MainActivity.this);
        initializeViews();
    }

    private void initializeViews() {
        deviceLocale = Locale.getDefault();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) mainToolbar.setElevation(0f);
        setSupportActionBar(mainToolbar);
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
        getSupportActionBar().setElevation(8f);
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) tabLayout.setElevation(8f);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_local_hospital_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_history_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_person_black_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_info_outline_black_24dp);
        tabLayout.getTabAt(0).getIcon().setColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(colorTabUnselected, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(colorTabUnselected, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(colorTabUnselected, PorterDuff.Mode.SRC_IN);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(colorTabUnselected, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        testFragment = (TestFragment) mainViewPagerAdapter.getItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(MainActivity.this, SearchTestsActivity.class));
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
