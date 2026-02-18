package com.example.automatedsystemssimulator;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.automatedsystemssimulator.managers.TestManager;
import com.example.automatedsystemssimulator.data.DataProvider;
import com.example.automatedsystemssimulator.data.Test;
import com.example.automatedsystemssimulator.utils.ProgressController;

import java.util.List;

public class TestTopicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_topics);

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Тесты");
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Test> tests = DataProvider.getTests();
        TestManager adapter = new TestManager(tests, test -> {
            ProgressController pm = new ProgressController(TestTopicsActivity.this);
            if (pm.getLastTestResult(test.getId()) != null) {
                Intent intent = new Intent(TestTopicsActivity.this, TestResultActivity.class);
                intent.putExtra("test_id", test.getId());
                startActivity(intent);
            } else {
                TestActivity.start(TestTopicsActivity.this, test.getId());
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}