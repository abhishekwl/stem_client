package io.github.abhishekwl.stemclient.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.stemclient.Adapters.MainViewPagerAdapter;
import io.github.abhishekwl.stemclient.Fragments.TestFragment;
import io.github.abhishekwl.stemclient.Models.TestItem;
import io.github.abhishekwl.stemclient.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.mainToolbar) Toolbar mainToolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.mainTabLayout) TabLayout tabLayout;
    @BindView(R.id.mainViewPager) ViewPager viewPager;

    @BindColor(R.color.colorAccentDark) int colorAccentDark;
    @BindColor(R.color.colorTabUnselected) int colorTabUnselected;
    @BindColor(android.R.color.white) int colorWhite;

    private Unbinder unbinder;
    private FirebaseAuth firebaseAuth;
    private TestFragment testFragment;
    private MainViewPagerAdapter mainViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(MainActivity.this);
        initializeViews();
    }

    private void initializeViews() {
        initializeNavigationDrawer();
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

    private void initializeNavigationDrawer() {
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, mainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) getWindow().setStatusBarColor(colorAccentDark);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class ExtractSelectedTests extends AsyncTask<ArrayList<TestItem>, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(ArrayList<TestItem>... arrayLists) {
            ArrayList<TestItem> testItemArrayList = arrayLists[0];
            ArrayList<String> testIdArrayList = new ArrayList<>();
            for (TestItem testItem : testItemArrayList) {
                Log.v(testItem.getTestName(), String.valueOf(testItem.isTestSelected()));
                if (testItem.isTestSelected()) testIdArrayList.add(testItem.getTestId());
            }
            return testIdArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                new ExtractSelectedTests().execute(testFragment.getTestItemArrayList());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
