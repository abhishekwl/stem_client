package io.github.abhishekwl.stemclient.Fragments;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindColor;
import butterknife.BindString;
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
    @BindString(R.string.base_server_url_tests) String serverUrl;

    private Unbinder unbinder;
    private View rootView;
    private ArrayList<TestItem> testItemArrayList = new ArrayList<>();
    private TestsRecyclerViewAdapter testsRecyclerViewAdapter;
    private MaterialDialog materialDialog;
    private RequestQueue requestQueue;
    private FusedLocationProviderClient fusedLocationProviderClient;

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
        requestQueue = Volley.newRequestQueue(rootView.getContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(rootView.getContext());
        initializeRecyclerView();
        retrieveDeviceLocation();
    }

    @SuppressLint("MissingPermission")
    private void retrieveDeviceLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location!=null) performNetworkRequest(location);
        });
    }

    private void performNetworkRequest(Location location) {
        testItemArrayList.clear();
        testsRecyclerViewAdapter.notifyDataSetChanged();
        testsProgressBar.setVisibility(View.VISIBLE);
        try {
            String district = getDeviceDistrict(location);
            district = URLEncoder.encode(district, "utf-8");
            serverUrl+="?popular=1"+(district==null?"":"&hospitalDistrict="+district);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, serverUrl, null, response -> {
                if(response!=null) {
                    for (int i=0; i<response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            TestItem testItem = convertJsonObjectToPojo(jsonObject);
                            testItemArrayList.add(testItem);
                        } catch (JSONException e) {
                            Snackbar.make(testsRecyclerView, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    if (response.length()==0) Snackbar.make(testsRecyclerView, "No hospital that offer tests found in your vicinity.", Snackbar.LENGTH_SHORT).show();
                    testsRecyclerViewAdapter.notifyDataSetChanged();
                } else Snackbar.make(testsRecyclerView, "There has been an error fetching data from the server.", Snackbar.LENGTH_SHORT).show();
                testsProgressBar.setVisibility(View.GONE);
            }, Throwable::printStackTrace);
            requestQueue.add(jsonArrayRequest);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private String getDeviceDistrict(Location location) {
        try {
            Geocoder geocoder = new Geocoder(rootView.getContext(), MainActivity.deviceLocale);
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size()>0) return addresses.get(0).getLocality();
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    private TestItem convertJsonObjectToPojo(JSONObject jsonObject) throws JSONException {
        TestItem testItem = new TestItem();
        testItem.setTestName(jsonObject.getString("testName"));
        testItem.setHospitalName(jsonObject.getString("hospitalName"));
        testItem.setTestPrice(jsonObject.getInt("testPrice"));
        testItem.setHospitalUid(jsonObject.getString("hospitalUid"));
        testItem.setTestId(jsonObject.getString("_id"));
        testItem.setHospitalLatitude(jsonObject.getDouble("hospitalLatitude"));
        testItem.setHospitalLongitude(jsonObject.getDouble("hospitalLongitude"));
        testItem.setHospitalImageUrl(jsonObject.getString("hospitalImageUrl"));
        return testItem;
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
            return arrayListSelected;
        }

        @Override
        protected void onPostExecute(ArrayList<TestItem> testItems) {
            super.onPostExecute(testItems);
            String dialogBody = "";
            String currencyCode = MainActivity.currency == null ? "\u20b9": MainActivity.currency.toString();
            int totalCost = 0;
            for (int i = 0; i < testItems.size(); i++) {
                TestItem currentTestItem = testItems.get(i);
                totalCost += currentTestItem.getTestPrice();
                dialogBody = dialogBody.concat((i + 1) + ". " + currentTestItem.getTestName() +" ("+currencyCode+" "+Integer.toString(currentTestItem.getTestPrice())+")\n");
            }
            dialogBody += "\nTOTAL COST = "+currencyCode+" " + totalCost;

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
