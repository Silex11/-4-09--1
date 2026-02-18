package com.example.automatedsystemssimulator;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.automatedsystemssimulator.managers.PracticeManager;
import com.example.automatedsystemssimulator.data.DataProvider;
import com.example.automatedsystemssimulator.data.PracticeScenario;
import java.util.List;

public class PracticeTopicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_topics);

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Тренировочные сценарии");
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<PracticeScenario> scenarios = DataProvider.getPracticeScenarios();
        PracticeManager adapter = new PracticeManager(scenarios, scenario -> {
            PracticeActivity.start(this, scenario.getId());
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}