
package hrishikesh.deshpande.mysocialapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by sanket on 11/17/2017.
 */

public class pagerAdapter extends FragmentStatePagerAdapter {
    int numberoftabs;

    public pagerAdapter(FragmentManager fm,int numberoftabs){
        super(fm);
        this.numberoftabs =numberoftabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                pendingRequest fri1 = new pendingRequest();
                return fri1;
            case 1:
                friendsFragment fri = new friendsFragment();
                return fri;
            case 2:
                requestFragment req = new requestFragment();
                return req;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return numberoftabs;
    }
}
