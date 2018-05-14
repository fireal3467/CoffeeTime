package com.example.emilylien.coffeetime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.emilylien.coffeetime.R;
import com.example.emilylien.coffeetime.data.DrinkInfo;

import java.util.List;

/**
 * Created by alanyu on 5/14/18.
 */



public class DrinkTypeAdapter extends RecyclerView.Adapter<DrinkTypeAdapter.ViewHolder>{

    private List<DrinkInfo> drinkList;
    private Context context;


    public DrinkTypeAdapter(List<DrinkInfo> drinkList, Context context){
        this.drinkList = drinkList;
        this.context = context;
        drinkList.add(new DrinkInfo("drink1", "100 ml", 300));
        drinkList.add(new DrinkInfo("drink2", "500 ml", 200));
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
        //TODO - add a dialog fragment to popup
    }


    @Override
    public int getItemCount() {
        return drinkList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView drinkName;

        public ViewHolder(View itemView) {
            super(itemView);
            drinkName = itemView.findViewById(R.id.drinkName);
        }
    }
}
