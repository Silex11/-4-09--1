package com.example.automatedsystemssimulator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.automatedsystemssimulator.managers.TestResultManager;
import com.example.automatedsystemssimulator.data.DataProvider;
import com.example.automatedsystemssimulator.data.Question;
import com.example.automatedsystemssimulator.data.Test;
import com.example.automatedsystemssimulator.utils.ProgressController;
import java.util.List;

public class TestResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Результат теста");
        }

        int testId = getIntent().getIntExtra("test_id", -1);
        if (testId == -1) {
            finish();
            return;
        }

        Test test = DataProvider.getTestById(testId);
        if (test == null) {
            finish();
            return;
        }

        ProgressController pm = new ProgressController(this);
        Bundle lastResult = pm.getLastTestResult(testId);
        if (lastResult == null) {
            finish();
            return;
        }

        int correct = lastResult.getInt("correct");
        int total = lastResult.getInt("total");
        int[] userAnswers = lastResult.getIntArray("userAnswers");
        int[] correctIndices = lastResult.getIntArray("correctIndices");

        List<Question> questions = test.getQuestions();

        TextView tvTitle = findViewById(R.id.tvTestTitle);
        TextView tvScore = findViewById(R.id.tvScore);
        TextView tvPercent = findViewById(R.id.tvPercent);
        TextView tvAttempts = findViewById(R.id.tvAttempts);
        Button btnRetry = findViewById(R.id.btnRetry);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewQuestions);

        tvTitle.setText(test.getTitle());
        tvScore.setText(String.format("Правильных ответов: %d из %d", correct, total));
        int percent = (int) ((correct * 100.0) / total);
        tvPercent.setText(String.format("Результат: %d%%", percent));

        int attempts = pm.getTestAttempts().getOrDefault(testId, 0);
        tvAttempts.setText(String.format("Количество попыток: %d", attempts));

        TestResultManager adapter = new TestResultManager(questions, userAnswers, correctIndices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnRetry.setOnClickListener(v -> {
            TestActivity.start(this, testId);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}