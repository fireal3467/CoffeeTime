package com.example.emilylien.coffeetime.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.emilylien.coffeetime.AddDrinkActivity;
import com.example.emilylien.coffeetime.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alanyu on 5/14/18.
 */

public class AddDrinkAdapter extends FragmentPagerAdapter {
    private final List<DrinkSelectionFragment> fragmentList = new ArrayList<>();

    public AddDrinkAdapter(FragmentManager fm) {
        super(fm);
        initLists();
    }

    private void initLists(){
        fragmentList.add(DrinkSelectionFragment.newInstance("Coffee",1));
        fragmentList.add(DrinkSelectionFragment.newInstance("Custom",2));
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).getSectionName();
    }


    public static class DrinkSelectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "SECTION_NUMBER";
        private static final String ARG_SECTION_NAME = "SECTION_NAME";

        private String sectionName;
        private int sectionNumber;

        public DrinkSelectionFragment() {
        }
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static AddDrinkAdapter.DrinkSelectionFragment newInstance(String sectionName, int sectionNumber) {
            AddDrinkAdapter.DrinkSelectionFragment fragment = new AddDrinkAdapter.DrinkSelectionFragment();
            fragment.setSectionName(sectionName);
            fragment.setSectionNumber(sectionNumber);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_drink, container, false);

            RecyclerView recyclerView = rootView.findViewById(R.id.recyclerList);
            recyclerView.setHasFixedSize(true);

            //TODO - this may not be safe?
            recyclerView.setLayoutManager(
                    new LinearLayoutManager(getActivity()));


            recyclerView.setAdapter(new DrinkTypeAdapter(new ArrayList(),getActivity()));
            System.out.println(getActivity().getClass().getName());

            return rootView;
        }

        public String getSectionName() {
            return sectionName;
        }

        public void setSectionName(String sectionName) {
            this.sectionName = sectionName;
        }

        public int getSectionNumber() {
            return sectionNumber;
        }

        public void setSectionNumber(int sectionNumber) {
            this.sectionNumber = sectionNumber;
        }
    }

}
