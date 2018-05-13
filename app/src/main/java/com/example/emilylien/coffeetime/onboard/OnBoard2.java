package com.example.emilylien.coffeetime.onboard;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.emilylien.coffeetime.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OnBoard2 extends AppCompatActivity {
    @BindView(R.id.btnOnboard2Next) Button btnOnboard2Next;
    @BindView(R.id.btnOnboard2Back) Button btnOnboard2Back;
    @BindView(R.id.tilAge) MaterialEditText tilAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board2);
        ButterKnife.bind(this);

        tilAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String regex = "^[1-9]\\d*$";
//                String input = s.toString();
//                if (!input.matches(regex)) {
//                    tilAge.setError("Invalid symbol");
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.btnOnboard2Next) void nextClicked() {
        Intent intent = new Intent(OnBoard2.this, OnBoard3.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnOnboard2Back) void backClicked() {
        Intent intent = new Intent(OnBoard2.this, OnBoard1.class);
        startActivity(intent);
    }

    @OnClick(R.id.rlLayout) void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
