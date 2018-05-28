package io.github.abhishekwl.stemclient.Fragments;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.github.abhishekwl.stemclient.Activities.MainActivity;
import io.github.abhishekwl.stemclient.Adapters.TestsRecyclerViewAdapter;
import io.github.abhishekwl.stemclient.Models.TestItem;
import io.github.abhishekwl.stemclient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {

    @BindView(R.id.testsRecyclerView)
    RecyclerView testsRecyclerView;
    @BindView(R.id.testsProgressBar)
    ProgressBar testsProgressBar;
    @BindColor(R.color.colorPrimary)
    int colorPrimary;
    @BindColor(R.color.colorAccent)
    int colorAccent;

    private Unbinder unbinder;
    private View rootView;
    private ArrayList<TestItem> testItemArrayList = new ArrayList<>();
    private TestsRecyclerViewAdapter testsRecyclerViewAdapter;
    private MaterialDialog materialDialog;

    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_test, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initializeViews();
        return rootView;
    }

    private void initializeViews() {
        initializeRecyclerView();
        new PushDummyData().execute();
    }

    private class PushDummyData extends AsyncTask<Void, Void, ArrayList<TestItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            testsProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<TestItem> doInBackground(Void... voids) {
            ArrayList<TestItem> testItemArrayList = new ArrayList<>();
            testItemArrayList.add(new TestItem("Glucose Test", "Test Hospital 1", 150, "123", "123"));
            testItemArrayList.add(new TestItem("Platelets Test", "Test Hospital 1", 250, "123", "123"));
            testItemArrayList.add(new TestItem("Urine Test", "Test Hospital 1", 350, "123", "123"));
            testItemArrayList.add(new TestItem("Diabetes Test", "Test Hospital 1", 450, "123", "123"));
            testItemArrayList.add(new TestItem("Health Checkup", "Test Hospital 1", 550, "123", "123"));
            testItemArrayList.add(new TestItem("Glucose Test", "Test Hospital 1", 150, "123", "123"));
            testItemArrayList.add(new TestItem("Platelets Test", "Test Hospital 1", 250, "123", "123"));
            testItemArrayList.add(new TestItem("Urine Test", "Test Hospital 1", 350, "123", "123"));
            testItemArrayList.add(new TestItem("Diabetes Test", "Test Hospital 1", 450, "123", "123"));
            testItemArrayList.add(new TestItem("Health Checkup", "Test Hospital 1", 550, "123", "123"));
            return testItemArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<TestItem> testItems) {
            super.onPostExecute(testItems);
            testItemArrayList = testItems;
            testsProgressBar.setVisibility(View.GONE);
            testsRecyclerViewAdapter = new TestsRecyclerViewAdapter(rootView.getContext(), testItemArrayList);
            testsRecyclerView.setAdapter(testsRecyclerViewAdapter);
        }
    }

    private void initializeRecyclerView() {
        testsRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        testsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        testsRecyclerView.setHasFixedSize(true);
        testsRecyclerViewAdapter = new TestsRecyclerViewAdapter(rootView.getContext(), testItemArrayList);
        testsRecyclerView.setAdapter(testsRecyclerViewAdapter);
    }

    private class ExtractSelectedTests extends AsyncTask<ArrayList<TestItem>, Void, ArrayList<TestItem>> {

        @Override
        protected ArrayList<TestItem> doInBackground(ArrayList<TestItem>... arrayLists) {
            ArrayList<TestItem> arrayListSelected = new ArrayList<>();
            for (TestItem testItem : arrayLists[0]) {
                if (testItem.isTestSelected()) arrayListSelected.add(testItem);
            }
            Log.v("SIZE_SELECTED", String.valueOf(arrayListSelected.size()));
            return arrayListSelected;
        }

        @Override
        protected void onPostExecute(ArrayList<TestItem> testItems) {
            super.onPostExecute(testItems);
            String dialogBody = "";
            int totalCost = 0;
            for (int i = 0; i < testItems.size(); i++) {
                TestItem currentTestItem = testItems.get(i);
                totalCost += currentTestItem.getTestPrice();
                dialogBody = dialogBody.concat((i + 1) + ". " + currentTestItem.getTestName() + " (\u20b9 " + Integer.toString(currentTestItem.getTestPrice()) + ")\n");
                Log.v("DIALOG_BODY", dialogBody);
            }
            dialogBody += "\nTOTAL COST = " + (MainActivity.currency == null ? "\u20b9" : MainActivity.currency.toString()) + " " + totalCost;

            if (testItems.isEmpty())
                Snackbar.make(testsRecyclerView, "Please select at least one test.", Snackbar.LENGTH_SHORT).show();
            else {
                materialDialog = new MaterialDialog.Builder(Objects.requireNonNull(getActivity()))
                        .title("SELECTED TESTS")
                        .content(dialogBody)
                        .positiveText("PROCEED")
                        .negativeText("CANCEL")
                        .titleColor(colorPrimary)
                        .positiveColor(colorPrimary)
                        .negativeColor(colorAccent)
                        .contentColor(Color.BLACK)
                        .onPositive((dialog, which) -> {

                        })
                        .onNegative((dialog, which) -> {
                            if (dialog.isShowing()) dialog.dismiss();
                        })
                        .show();
            }
        }
    }

    public ArrayList<TestItem> getTestItemArrayList() {
        return testItemArrayList;
    }

    @OnClick(R.id.testsNextButton)
    public void onNextButtonPressed() {
        new ExtractSelectedTests().execute(testItemArrayList);
    }

    @Override
    public void onStart() {
        super.onStart();
        unbinder = ButterKnife.bind(this, rootView);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
