package com.example.automatedsystemssimulator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.automatedsystemssimulator.data.DataProvider;
import com.example.automatedsystemssimulator.data.Option;
import com.example.automatedsystemssimulator.data.Question;
import com.example.automatedsystemssimulator.data.Test;
import com.example.automatedsystemssimulator.utils.ProgressController;
import java.util.Arrays;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private static final String EXTRA_TEST_ID = "test_id";

    private com.google.android.material.progressindicator.LinearProgressIndicator progressIndicator;
    private Test test;
    private int currentQuestionIndex = 0;
    private int[] userAnswers;
    private ProgressController progressManager;

    private TextView tvProgress, tvQuestion;
    private RadioGroup radioGroup;
    private RadioButton rb1, rb2, rb3;
    private Button btnNext, btnFinish;

    public static void start(android.content.Context context, int testId) {
        Intent intent = new Intent(context, TestActivity.class);
        intent.putExtra(EXTRA_TEST_ID, testId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Тестирование");
        }

        int testId = getIntent().getIntExtra(EXTRA_TEST_ID, -1);
        test = DataProvider.getTestById(testId);
        if (test == null) {
            finish();
            return;
        }

        progressManager = new ProgressController(this);
        userAnswers = new int[test.getQuestions().size()];
        Arrays.fill(userAnswers, -1);

        tvProgress = findViewById(R.id.tvProgress);
        tvQuestion = findViewById(R.id.tvQuestion);
        radioGroup = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.radioButton1);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        btnNext = findViewById(R.id.btnNext);
        btnFinish = findViewById(R.id.btnFinish);

        progressIndicator = findViewById(R.id.progressIndicator);


        loadQuestion(currentQuestionIndex);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                int selectedIndex = -1;
                if (checkedId == R.id.radioButton1) selectedIndex = 0;
                else if (checkedId == R.id.radioButton2) selectedIndex = 1;
                else if (checkedId == R.id.radioButton3) selectedIndex = 2;
                userAnswers[currentQuestionIndex] = selectedIndex;
                updateProgress();

                if (currentQuestionIndex == test.getQuestions().size() - 1) {
                    btnFinish.setEnabled(true);
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            if (userAnswers[currentQuestionIndex] == -1) {
                Toast.makeText(TestActivity.this, "Выберите ответ!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentQuestionIndex < test.getQuestions().size() - 1) {
                currentQuestionIndex++;
                loadQuestion(currentQuestionIndex);
            }
        });

        btnFinish.setOnClickListener(v -> {
            if (userAnswers[currentQuestionIndex] == -1) {
                Toast.makeText(TestActivity.this, "Выберите ответ на последний вопрос!", Toast.LENGTH_SHORT).show();
                return;
            }
            finishTest();
        });

        btnFinish.setEnabled(false);
    }

    private void updateProgress() {
        int total = test.getQuestions().size();
        int answered = 0;
        for (int ans : userAnswers) {
            if (ans != -1) answered++;
        }
        int progress = (answered * 100) / total;
        progressIndicator.setProgress(progress);
    }
    private void loadQuestion(int index) {
        Question q = test.getQuestions().get(index);
        tvQuestion.setText(q.getText());

        java.util.List<Option> opts = q.getOptions();
        rb1.setText(opts.get(0).getText());
        rb2.setText(opts.get(1).getText());
        rb3.setText(opts.get(2).getText());

        tvProgress.setText(String.format("Вопрос %d из %d", index + 1, test.getQuestions().size()));

        if (userAnswers[index] != -1) {
            switch (userAnswers[index]) {
                case 0: radioGroup.check(R.id.radioButton1); break;
                case 1: radioGroup.check(R.id.radioButton2); break;
                case 2: radioGroup.check(R.id.radioButton3); break;
            }
        } else {
            radioGroup.clearCheck();
        }
        updateProgress();

        if (index == test.getQuestions().size() - 1) {
            btnFinish.setEnabled(userAnswers[index] != -1);
        } else {
            btnFinish.setEnabled(false);
        }
    }

    private void finishTest() {
        int correctCount = 0;
        List<Question> questions = test.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers[i] == questions.get(i).getCorrectIndex()) {
                correctCount++;
            }
        }

        ProgressController pm = new ProgressController(this);
        pm.saveTestAttempt(test.getId(), correctCount, questions.size());

        int[] correctIndices = new int[questions.size()];
        for (int i = 0; i < questions.size(); i++) {
            correctIndices[i] = questions.get(i).getCorrectIndex();
        }
        pm.saveLastTestResult(test.getId(), correctCount, questions.size(), userAnswers, correctIndices);

        Intent intent = new Intent(this, TestResultActivity.class);
        intent.putExtra("test_id", test.getId());
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}