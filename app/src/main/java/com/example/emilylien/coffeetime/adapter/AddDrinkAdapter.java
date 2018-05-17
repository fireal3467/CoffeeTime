package com.example.emilylien.coffeetime.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.emilylien.coffeetime.AddDrinkActivity;
import com.example.emilylien.coffeetime.AddDrinkTypeDialog;
import com.example.emilylien.coffeetime.R;
import com.example.emilylien.coffeetime.data.AppDatabase;
import com.example.emilylien.coffeetime.data.DrinkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alanyu on 5/14/18.
 */

public class AddDrinkAdapter extends FragmentPagerAdapter{

    public static final String SECTION_NUMBER = "SECTION_NUMBER";

    private final List<DrinkSelectionFragment> fragmentList = new ArrayList<>();

    private FragmentManager fm;

    public AddDrinkAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
        initLists();
    }

    private void initLists(){
        fragmentList.add(DrinkSelectionFragment.newInstance("Coffee",0,this));
        fragmentList.add(DrinkSelectionFragment.newInstance("Custom",1, this));
    }

    @Override
    public Fragment getItem(int position) {
        DrinkSelectionFragment fragment = fragmentList.get(position);
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


    public void addDrinkType(DrinkInfo drink, int fragPos) {
        DrinkSelectionFragment fragment = fragmentList.get(fragPos);
        fragment.addDrinkType(drink);
    }


    private void showAddDrinkTypeDialog(int sectionNumber) {
        AddDrinkTypeDialog dialog = new AddDrinkTypeDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(SECTION_NUMBER,sectionNumber);
        dialog.setArguments(bundle);

        dialog.show(fm,"stuff");
    }


    public static class DrinkSelectionFragment
            extends Fragment {

        private String sectionName;
        private int sectionNumber;
        private DrinkTypeAdapter drinkTypeAdapter;
        public AddDrinkAdapter parentAdapter;
        private RecyclerView recyclerView;


        public DrinkSelectionFragment() { }

        public static AddDrinkAdapter.DrinkSelectionFragment newInstance(
                String sectionName,
                int sectionNumber,
                AddDrinkAdapter adapter
        ) {
            AddDrinkAdapter.DrinkSelectionFragment fragment = new AddDrinkAdapter.DrinkSelectionFragment();
            fragment.parentAdapter = adapter;
            fragment.setSectionName(sectionName);
            fragment.setSectionNumber(sectionNumber);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_drink, container, false);

            Button addDrinkTypebtn = rootView.findViewById(R.id.btnAddDrinkType);

            addDrinkTypebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentAdapter.showAddDrinkTypeDialog(sectionNumber);
                }
            });
            recyclerView = rootView.findViewById(R.id.recyclerList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(
                    new LinearLayoutManager(getActivity()));

            initDrinks();

            return rootView;
        }


        private void initDrinks(){
            new Thread(){
                @Override
                public void run() {
                    final List<DrinkInfo> drinks =
                            AppDatabase.getAppDatabase(getActivity()).drinkDAO().getAllForCategory(sectionNumber);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            drinkTypeAdapter = new DrinkTypeAdapter(drinks, getActivity(), sectionNumber);
                            recyclerView.setAdapter(drinkTypeAdapter);
                        }
                    });
                }
            }.start();
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

        public void addDrinkType(DrinkInfo drink) {
            drinkTypeAdapter.addDrinkType(drink);
        }
    }
}
