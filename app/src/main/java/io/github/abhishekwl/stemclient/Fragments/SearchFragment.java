package io.github.abhishekwl.stemclient.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.abhishekwl.stemclient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private View rootView;
    private Unbinder unbinder;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
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
