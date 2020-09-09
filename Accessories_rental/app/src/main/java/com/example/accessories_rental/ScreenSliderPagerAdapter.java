package com.example.accessories_rental;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;



public class ScreenSliderPagerAdapter  extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 5;


    public ScreenSliderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new ProductNameFragment();
            case 1:
                return new ProductPictureFragment();
            case 2:
                return new SelectTimeFragment();
            case 3:
                return new SelectDaysFragment();
            case 4:
                return new PriceFragment();
            default:
                return new ProductNameFragment();
        }
    }

    public int getCount() {

        return NUM_PAGES;
    }
}

