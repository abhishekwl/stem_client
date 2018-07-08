package io.github.abhishekwl.stemclient.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import io.github.abhishekwl.stemclient.Activities.CheckoutActivity;
import io.github.abhishekwl.stemclient.Activities.MainActivity;
import io.github.abhishekwl.stemclient.Adapters.TestsRecyclerViewAdapter;
import io.github.abhishekwl.stemclient.Helpers.ApiClient;
import io.github.abhishekwl.stemclient.Helpers.ApiInterface;
import io.github.abhishekwl.stemclient.Models.Test;
import io.github.abhishekwl.stemclient.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        rootView = inflater.inflate(R.layout.fragment_test, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initializeViews();
        return rootView;
    }

    private void initializeViews() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(rootView.getContext());
        firebaseAuth = FirebaseAuth.getInstance();
        initializeRecyclerView();
        checkPermissions();
    }

    private void checkPermissions() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        retrieveDeviceLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Snackbar.make(testsRecyclerView, "Please give us access to location services so that we know where you are", Snackbar.LENGTH_INDEFINITE)
                        .setActionTextColor(Color.YELLOW)
                        .setAction("GRANT", v -> {
                            checkPermissions();
                        }).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    @SuppressLint("MissingPermission")
    private void retrieveDeviceLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location!=null) {
                String deviceCity = getDeviceDistrict(location);
                if (!TextUtils.isEmpty(deviceCity)) fetchPopularTests(location.getLatitude(), location.getLongitude(), deviceCity);
            }
        }).addOnFailureListener(e -> {
            Snackbar.make(testsRecyclerView, "Please make sure location services are active.", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.YELLOW)
                    .setAction("SETTINGS", v -> {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }).show();
        });
    }

    private void fetchPopularTests(double latitude, double longitude, String deviceCity) {
        testArrayList.clear();
        testsRecyclerViewAdapter.notifyDataSetChanged();
        testsProgressBar.setVisibility(View.VISIBLE);

        if (apiInterface==null) apiInterface = ApiClient.getClient(rootView.getContext()).create(ApiInterface.class);
        apiInterface.getPopularTests(firebaseAuth.getUid(), deviceCity).enqueue(new Callback<ArrayList<Test>>() {
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
                Snackbar.make(testsRecyclerView, t.getMessage(), Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.YELLOW)
                        .setAction("RETRY", v -> fetchPopularTests(latitude, longitude, deviceCity)).show();
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

    private class TestArrayListFilter extends AsyncTask<ArrayList<Test>, Void, ArrayList<Test>> {

      @Override
      protected ArrayList<Test> doInBackground(ArrayList<Test>... arrayLists) {
        ArrayList<Test> filteredList = new ArrayList<>();
        for (Test test: arrayLists[0]) if (test.isTestSelected()) filteredList.add(test);
        for (int i=0; i<filteredList.size(); i++) {
          for (int j=0; j<=i; j++) {
            if (i!=j && (filteredList.get(i).getTestHospital().getHospitalUid().equals(filteredList.get(j).getTestHospital().getHospitalUid()))) return null;
          }
        }
        return filteredList;
      }

      @Override
      protected void onPostExecute(ArrayList<Test> tests) {
        super.onPostExecute(tests);
        if (tests==null) Snackbar.make(testsRecyclerView, "Please select tests from the same hospital", Snackbar.LENGTH_SHORT).show();
        else if (tests.isEmpty()) Snackbar.make(testsRecyclerView, "Please select tests to be added to Cart.", Snackbar.LENGTH_SHORT).show();
        else {
          Intent checkoutTestsIntent = new Intent(getActivity(), CheckoutActivity.class);
          checkoutTestsIntent.putParcelableArrayListExtra("TESTS", tests);
          startActivity(checkoutTestsIntent);
        }
      }
    }

    @OnClick(R.id.testsNextButton)
    public void onNextButtonPressed() {
      //noinspection unchecked
      new TestArrayListFilter().execute(testArrayList);
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
