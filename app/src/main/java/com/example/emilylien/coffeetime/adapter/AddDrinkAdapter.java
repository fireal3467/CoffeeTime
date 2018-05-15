package com.example.emilylien.coffeetime.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.emilylien.coffeetime.AddDrinkActivity;
import com.example.emilylien.coffeetime.AddDrinkTypeDialog;
import com.example.emilylien.coffeetime.R;
import com.example.emilylien.coffeetime.data.DrinkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alanyu on 5/14/18.
 */

public class AddDrinkAdapter extends FragmentPagerAdapter{
    private final List<DrinkSelectionFragment> fragmentList = new ArrayList<>();

    FragmentManager fm;

    public AddDrinkAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
        initLists();
    }

    private void initLists(){
        fragmentList.add(DrinkSelectionFragment.newInstance("Coffee",1,this));
        fragmentList.add(DrinkSelectionFragment.newInstance("Custom",2, this));
    }

    @Override
    public Fragment getItem(int position) {
        DrinkSelectionFragment fragment = fragmentList.get(position);

        System.out.println("\n\n getItem \n\n");
        return fragment;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).getSectionName();
    }

    private void addDrink() {
        new AddDrinkTypeDialog().show(fm,"stuff");
    }


    public static class DrinkSelectionFragment
            extends Fragment
            implements AddDrinkTypeDialog.AddDrinkTypeInterface {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "SECTION_NUMBER";
        private static final String ARG_SECTION_NAME = "SECTION_NAME";

        private String sectionName;
        private int sectionNumber;
        private DrinkTypeAdapter drinkTypeAdapter;
        //TODO - fix later
        public AddDrinkAdapter adapter;


        public DrinkSelectionFragment() {
            System.out.println("\n\n constructor \n\n");
        }
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static AddDrinkAdapter.DrinkSelectionFragment newInstance(String sectionName, int sectionNumber, AddDrinkAdapter adapter) {
            System.out.println("\n\n newInstance \n\n");
            AddDrinkAdapter.DrinkSelectionFragment fragment = new AddDrinkAdapter.DrinkSelectionFragment();
            fragment.adapter = adapter;
            fragment.setSectionName(sectionName);
            fragment.setSectionNumber(sectionNumber);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            System.out.println("\n\n OnCreateView \n\n");

            View rootView = inflater.inflate(R.layout.fragment_add_drink, container, false);

            Button addDrinkTypebtn = rootView.findViewById(R.id.btnAddDrinkType);
            addDrinkTypebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.addDrink();
                }
            });

            RecyclerView recyclerView = rootView.findViewById(R.id.recyclerList);
            recyclerView.setHasFixedSize(true);
            //TODO - this may not be safe, look into this later
            recyclerView.setLayoutManager(
                    new LinearLayoutManager(getActivity()));
            drinkTypeAdapter = new DrinkTypeAdapter(new ArrayList(), getActivity());
            recyclerView.setAdapter(drinkTypeAdapter);

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

        @Override
        public void addDrinkType(DrinkInfo drink) {
            System.out.println("Drink Added");
        }
    }
}
