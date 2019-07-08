package com.andela.bookmarkit.ui.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.andela.bookmarkit.MainActivity;
import com.andela.bookmarkit.R;
import com.andela.bookmarkit.data.model.City;
import com.andela.bookmarkit.ui.cities.CitiesFragment;
import com.andela.bookmarkit.ui.cities.details.CityDetailsFragment;
import com.andela.bookmarkit.ui.map.MapFragment;


public class FragmentSwitcher implements FragmentSwitcherInterface {
    private MainActivity activity;
    private FragmentManager fragmentManager;

    public FragmentSwitcher(MainActivity activity, FragmentManager fragmentManager) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
    }

    private void addFullScreenFragment(BaseFragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            fragmentManager.beginTransaction()
                    .addToBackStack(fragment.getTag())
                    .replace(R.id.home_full_screen_container, fragment, fragment.getTag())
                    .commitAllowingStateLoss();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.home_full_screen_container, fragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void removeFragment(String tag) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
            fragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void showMapFragment() {
        addFullScreenFragment(MapFragment.newInstance(), false);
    }

    @Override
    public void showDetailsFragment(City city) {
        addFullScreenFragment(CityDetailsFragment.newInstance(city), true);
    }

    @Override
    public void showCitiesFragment() {
        addFullScreenFragment(CitiesFragment.newInstance(), true);
    }
}
