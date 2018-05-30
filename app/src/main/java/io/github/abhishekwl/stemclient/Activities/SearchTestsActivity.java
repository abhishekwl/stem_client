package io.github.abhishekwl.stemclient.Activities;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.stemclient.Adapters.TestsRecyclerViewAdapter;
import io.github.abhishekwl.stemclient.Models.TestItem;
import io.github.abhishekwl.stemclient.R;

public class SearchTestsActivity extends AppCompatActivity {

    @BindView(R.id.searchTestsRecyclerView) RecyclerView searchTestsRecyclerView;
    @BindView(R.id.searcTestsProgressBar) ProgressBar progressBar;
    @BindView(R.id.searchTestsNextFAB) FloatingActionButton searchTestsNextFAB;

    private Unbinder unbinder;
    private TestsRecyclerViewAdapter testsRecyclerViewAdapter;
    private ArrayList<TestItem> testItemArrayList = new ArrayList<>();
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tests);

        unbinder = ButterKnife.bind(SearchTestsActivity.this);
        initializeViews();
    }

    private void initializeViews() {
        firebaseAuth = FirebaseAuth.getInstance();
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        searchTestsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchTestsRecyclerView.setHasFixedSize(true);
        searchTestsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        testsRecyclerViewAdapter = new TestsRecyclerViewAdapter(getApplicationContext(), testItemArrayList);
        searchTestsRecyclerView.setAdapter(testsRecyclerViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_tests_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(SearchTestsActivity.this.getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) searchView.setIconified(true);
                searchItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
