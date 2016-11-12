package com.example.learningzoo.xplorerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void toChoose(View v) {
        Intent intent = new Intent(this, ChooseDestinationsActivity.class);
        startActivity(intent);
    }

    protected void toExpense(View v) {
        startActivity(new Intent(this, ExpensesManagerActivity.class));
    }
}
