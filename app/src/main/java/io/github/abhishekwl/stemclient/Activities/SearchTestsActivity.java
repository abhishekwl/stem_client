package io.github.abhishekwl.stemclient.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.stemclient.Adapters.TestsRecyclerViewAdapter;
import io.github.abhishekwl.stemclient.Models.TestItem;
import io.github.abhishekwl.stemclient.R;

public class SearchTestsActivity extends AppCompatActivity {

    @BindView(R.id.searchTestsRecyclerView)
    RecyclerView searchTestsRecyclerView;
    @BindView(R.id.searchTestsSearchView)
    SearchView searchView;
    @BindColor(R.color.colorPrimary) int colorPrimary;
    @BindColor(R.color.colorTextDark) int colorTextDark;

    private Unbinder unbinder;
    private FirebaseAuth firebaseAuth;
    private ArrayList<TestItem> testItemArrayList = new ArrayList<>();
    private TestsRecyclerViewAdapter testsRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tests);

        unbinder = ButterKnife.bind(SearchTestsActivity.this);
        initializeViews();
    }

    private void initializeViews() {
        firebaseAuth = FirebaseAuth.getInstance();
        initializeSearchView();
        initializeRecyclerView();
    }

    private void initializeSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()) searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initializeRecyclerView() {
        searchTestsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchTestsRecyclerView.setHasFixedSize(true);
        searchTestsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        testsRecyclerViewAdapter = new TestsRecyclerViewAdapter(getApplicationContext(), testItemArrayList);
        searchTestsRecyclerView.setAdapter(testsRecyclerViewAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!searchView.isIconified()) searchView.setIconified(true);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
