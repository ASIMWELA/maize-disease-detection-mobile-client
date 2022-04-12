package com.detection.diseases.maize.ui.account;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * @author Augustine
 *
 *<p>
 *     An Adapter class that manages tabs of the user account fragment
 *
 *
 *
 *</p>
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    /**
     * Total number of tabs to be managed by the adapter
     */
    int numberOfTabs;

    /**
     * Constructor
     *
     * @param fm Fragment manager for the fragments to be managed by the adapter
     *
     * @param numberOfTabs Total number of tabs to be managed by the adapter
     */
    public FragmentAdapter(@NonNull FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }


    /**
     * Return the active adapter
     *
     * @param position The position of the active adapter
     *
     * @return {@link Fragment}
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UserLoggedInInvolvement();
            case 1:
                return new UserLoggedinAbout();
            default:
                return null;
        }
    }

    /**
     * Gets track of the total number of fragments
     *
     * @return Total number of fragments
     */
    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
