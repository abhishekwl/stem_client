package io.github.abhishekwl.stemclient.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.abhishekwl.stemclient.Fragments.AboutFragment;
import io.github.abhishekwl.stemclient.Fragments.HistoryFragment;
import io.github.abhishekwl.stemclient.Fragments.ProfileFragment;
import io.github.abhishekwl.stemclient.Fragments.TestFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new TestFragment();
            case 1: return new HistoryFragment();
            case 2: return new ProfileFragment();
            default: return new AboutFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
