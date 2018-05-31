package io.github.abhishekwl.stemclient.Activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindDrawable;
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
    @BindColor(R.color.colorPrimary) int colorPrimary;
    @BindColor(R.color.colorTextDark) int colorTextDark;
    @BindDrawable(R.drawable.ic_arrow_back_black_24dp) Drawable backIconDrawable;

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
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
