package com.example.emilylien.coffeetime.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.emilylien.coffeetime.AddDrinkActivity;
import com.example.emilylien.coffeetime.AddDrinkDialog;
import com.example.emilylien.coffeetime.R;
import com.example.emilylien.coffeetime.data.DrinkInfo;

import java.util.List;

/**
 * Created by alanyu on 5/14/18.
 */


public class DrinkTypeAdapter extends RecyclerView.Adapter<DrinkTypeAdapter.ViewHolder>{

    public static final String DRINK_CHOICE = "DRINK_CHOICE";
    private List<DrinkInfo> drinkList;
    private Context context;
    private int category;

    /*
    @param drinkList - should contain all the prerecorded default drinks to be added
                       in addition to any custom drinks added
     */
    public DrinkTypeAdapter(List<DrinkInfo> drinkList, Context context, int category){
        this.drinkList = drinkList;
        this.context = context;
        this.category = category;
        if(category == 0) { //Only do this on the Coffee Tab
            drinkList.add(new DrinkInfo("Americano", "237 ml", 63, category));
            drinkList.add(new DrinkInfo("Brewed", "237 ml", 95, category));
            drinkList.add(new DrinkInfo("Cappucino", "237 ml", 3, category));
            drinkList.add(new DrinkInfo("Decaf", "237 ml", 63, category));
            drinkList.add(new DrinkInfo("Espresso", "40 ml", 63, category));
            drinkList.add(new DrinkInfo("Latte", "237 ml", 63, category));
            drinkList.add(new DrinkInfo("Macchiato", "237 ml", 63, category));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drink_info_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.drinkName.setText(
                drinkList.get(holder.getAdapterPosition()).getDrinkName()
        );
        setOnClickListeners(holder,position);
    }

    private void setOnClickListeners(final ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDrinkDialog newDialog = new AddDrinkDialog();
                Bundle bundle = new Bundle();
                bundle.putSerializable(
                        DRINK_CHOICE,
                        drinkList.get(holder.getAdapterPosition()));
                newDialog.setArguments(bundle);

                newDialog.show(((AddDrinkActivity)context).getSupportFragmentManager()
                        , "AddDrink");
            }
        });
    }


    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    public void addDrinkType(DrinkInfo drink){
        drinkList.add(0, drink);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView drinkName;

        public ViewHolder(View itemView) {
            super(itemView);
            drinkName = itemView.findViewById(R.id.drinkName);
        }
    }
}
