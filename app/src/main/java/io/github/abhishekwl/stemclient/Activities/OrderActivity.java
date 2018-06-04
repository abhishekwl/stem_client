package io.github.abhishekwl.stemclient.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.stemclient.Models.TestItem;
import io.github.abhishekwl.stemclient.R;

public class OrderActivity extends AppCompatActivity {

    private Unbinder unbinder;
    private ArrayList<TestItem> testItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        unbinder = ButterKnife.bind(OrderActivity.this);
        initializeViews();
    }

    private void initializeViews() {
        testItemArrayList = getIntent().getParcelableArrayListExtra("SELECTED_TESTS");
        for (TestItem testItem: testItemArrayList) Log.v(testItem.getTestName(), String.valueOf(testItem.getTestPrice()));
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
