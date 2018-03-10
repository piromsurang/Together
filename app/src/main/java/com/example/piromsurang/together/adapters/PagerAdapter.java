package com.example.piromsurang.together.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.piromsurang.together.FriendFragment;
import com.example.piromsurang.together.InvitationFragment;
import com.example.piromsurang.together.ProfileFragment;
import com.example.piromsurang.together.models.Invitation;

/**
 * Created by piromsurang on 10/3/2018 AD.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;

    public PagerAdapter(FragmentManager fragmentManager, int numberOfTabs) {
        super(fragmentManager);
        this.numberOfTabs = numberOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                InvitationFragment invitationFragment = new InvitationFragment();
                return invitationFragment;
            case 1:
                FriendFragment friendFragment = new FriendFragment();
                return friendFragment;
            case 2:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

}
