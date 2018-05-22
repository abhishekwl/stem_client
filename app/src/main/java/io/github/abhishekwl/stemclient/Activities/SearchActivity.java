package io.github.abhishekwl.stemclient.Activities;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.stemclient.R;

public class SearchActivity extends AppCompatActivity {

    private Unbinder unbinder;
    private ArrayList<String> selectedTestIdsArrayList;
    private android.support.v7.widget.SearchView testsSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        unbinder = ButterKnife.bind(SearchActivity.this);
        initializeViews();
    }

    private void initializeViews() {
        selectedTestIdsArrayList = getIntent().getStringArrayListExtra("SELECTED_TEST_IDS");
        Log.v("SEARCH_ACT", String.valueOf(selectedTestIdsArrayList.size()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        testsSearchView = (android.support.v7.widget.SearchView) menuItem.getActionView();
        testsSearchView.setQueryHint("Search tests");
        if (searchManager != null)
            testsSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        testsSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
