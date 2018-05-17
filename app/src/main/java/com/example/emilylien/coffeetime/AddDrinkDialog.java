package com.example.emilylien.coffeetime;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emilylien.coffeetime.data.DrinkInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.emilylien.coffeetime.adapter.DrinkTypeAdapter.DRINK_CHOICE;

/**
 * Created by alanyu on 5/14/18.
 */

public class AddDrinkDialog extends DialogFragment {

    TextView drinkName;
    TextView drinkSize;
    TextView caffineAmount;
    Button cancelbtn;
    Button addBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_drink_dialog, container, false);
        drinkName = v.findViewById(R.id.tvDrinkName);
        drinkSize = v.findViewById(R.id.tvDrinkSize);
        caffineAmount = v.findViewById(R.id.tvCaffineAmount);
        cancelbtn = v.findViewById(R.id.btnCancel);
        addBtn = v.findViewById(R.id.btnAddDrink);

        initOnClickListener();
        setDrinkInfo();

        return v;
    }

    private void initOnClickListener(){
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrinkInfo drink = (DrinkInfo) getArguments().getSerializable(DRINK_CHOICE);
                System.out.println(getContext().getClass().getName());
                ((AddDrinkActivity) getContext()).addDrink(drink);
                dismiss();
            }
        });
    }

    private void setDrinkInfo(){
        DrinkInfo drink = (DrinkInfo) getArguments().getSerializable(DRINK_CHOICE);
        drinkName.setText(drink.getDrinkName());
        drinkSize.setText(drink.getSize());
        caffineAmount.setText(Integer.toString(drink.getCaffineAmount()) + "ml");
    }

}