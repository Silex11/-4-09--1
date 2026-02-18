package com.example.automatedsystemssimulator;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.cardTheory).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, TheoryTopicsActivity.class)));

        findViewById(R.id.cardSimulator).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, PracticeTopicsActivity.class))); // ИСПРАВЛЕНО

        findViewById(R.id.cardTest).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, TestTopicsActivity.class)));

        findViewById(R.id.cardResults).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ResultsActivity.class)));

        findViewById(R.id.cardProgress).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ProgressActivity.class)));
    }
}