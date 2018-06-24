package io.github.abhishekwl.stemclient.Fragments;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.stemclient.Activities.MainActivity;
import io.github.abhishekwl.stemclient.Adapters.TestsRecyclerViewAdapter;
import io.github.abhishekwl.stemclient.Helpers.ApiClient;
import io.github.abhishekwl.stemclient.Helpers.ApiInterface;
import io.github.abhishekwl.stemclient.Models.Test;
import io.github.abhishekwl.stemclient.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ApiInterface apiInterface;
    private FirebaseAuth firebaseAuth;
    private TestsRecyclerViewAdapter testsRecyclerViewAdapter;
    private ArrayList<Test> testArrayList = new ArrayList<>();

    public TestFragment() {
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(rootView.getContext());
        firebaseAuth = FirebaseAuth.getInstance();
        initializeRecyclerView();
        retrieveDeviceLocation();
    }

    @SuppressLint("MissingPermission")
    private void retrieveDeviceLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location!=null) {
                String deviceCity = getDeviceDistrict(location);
                if (!TextUtils.isEmpty(deviceCity)) fetchPopularTests(location.getLatitude(), location.getLongitude(), deviceCity);
            }
        });
    }

    private void fetchPopularTests(double latitude, double longitude, String deviceCity) {
        testArrayList.clear();
        testsRecyclerViewAdapter.notifyDataSetChanged();
        testsProgressBar.setVisibility(View.VISIBLE);

        if (apiInterface==null) apiInterface = ApiClient.getClient(rootView.getContext()).create(ApiInterface.class);
        //TODO: Update Firebase UID being passed.
        apiInterface.getPopularTests("testclient", deviceCity).enqueue(new Callback<ArrayList<Test>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Test>> call, @NonNull Response<ArrayList<Test>> response) {
                testArrayList.addAll(response.body());
                testsProgressBar.setVisibility(View.GONE);
                testsRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Test>> call, @NonNull Throwable t) {
                testArrayList.clear();
                testsProgressBar.setVisibility(View.GONE);
                testsRecyclerViewAdapter.notifyDataSetChanged();
                Snackbar.make(testsRecyclerView, "There has been an error fetching data from the cloud :(", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.YELLOW)
                        .setAction("RETRY", v -> {
                            fetchPopularTests(latitude, longitude, deviceCity);
                        }).show();
            }
        });
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

    private void initializeRecyclerView() {
        testsRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        testsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        testsRecyclerView.setHasFixedSize(true);
        testsRecyclerViewAdapter = new TestsRecyclerViewAdapter(rootView.getContext(), testArrayList);
        testsRecyclerView.setAdapter(testsRecyclerViewAdapter);
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
