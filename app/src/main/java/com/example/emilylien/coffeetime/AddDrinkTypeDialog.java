package com.example.emilylien.coffeetime;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emilylien.coffeetime.data.DrinkInfo;

import static com.example.emilylien.coffeetime.adapter.AddDrinkAdapter.SECTION_NUMBER;
import static com.example.emilylien.coffeetime.adapter.DrinkTypeAdapter.DRINK_CHOICE;

/**
 * Created by alanyu on 5/15/18.
 */

public class AddDrinkTypeDialog extends DialogFragment {

    public interface AddDrinkTypeInterface {
        public void addDrinkType(DrinkInfo drink, int sectionNumber);
    }

    private AddDrinkTypeInterface addDrinkTypeInterface;

    private EditText drinkName;
    private EditText drinkSize;
    private EditText caffineAmount;
    private Button cancelbtn;
    private Button addBtn;

    private int sectionNum;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        System.out.println(context.getClass().getName());

        if (context instanceof AddDrinkTypeInterface) {
            addDrinkTypeInterface = (AddDrinkTypeInterface)context;
        } else {
            throw new RuntimeException(
                    "The Activity does not implement the AddDrinkTypeInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_drink_type_dialog, container, false);
        drinkName = v.findViewById(R.id.etDrinkName);
        drinkSize = v.findViewById(R.id.etDrinkSize);
        caffineAmount = v.findViewById(R.id.etCaffineAmount);
        cancelbtn = v.findViewById(R.id.btnCancel);
        addBtn = v.findViewById(R.id.btnAddDrink);
        sectionNum = getArguments().getInt(SECTION_NUMBER);

        initOnClickListeners();

        return v;
    }

    private void initOnClickListeners(){
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!fieldValid()){
                    dismiss();
                }else {
                    DrinkInfo newDrink = new DrinkInfo(
                            drinkName.getText().toString(),
                            drinkSize.getText().toString(),
                            Integer.parseInt(caffineAmount.getText().toString()),
                            sectionNum
                    );
                    addDrinkTypeInterface.addDrinkType(newDrink, sectionNum);
                    dismiss();
                }
            }
        });
    }

    private boolean fieldValid(){
        if(drinkName.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Name Field can't be Empty", Toast.LENGTH_SHORT).show();
            return false;
        }else if(drinkSize.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Size Field can't be Empty", Toast.LENGTH_SHORT).show();
            return false;
        }else if(caffineAmount.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Caffine amount Field can't be Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
