package com.example.automatedsystemssimulator;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.automatedsystemssimulator.data.DataProvider;
import com.example.automatedsystemssimulator.data.Lecture;
import com.example.automatedsystemssimulator.data.PracticeScenario;
import com.example.automatedsystemssimulator.data.Test;
import com.example.automatedsystemssimulator.utils.ProgressController;
import java.util.List;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity {

    private ProgressController progressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Мои результаты");
        }

        progressManager = new ProgressController(this);

        Map<Integer, Integer> attempts = progressManager.getTestAttempts();
        Map<Integer, Integer> bestScores = progressManager.getTestBestScores();

        int totalTests = DataProvider.getTests().size();
        int completedTests = 0;
        int totalScoreSum = 0;
        int totalMaxScore = 0;

        for (Test test : DataProvider.getTests()) {
            int testId = test.getId();
            if (attempts.containsKey(testId)) {
                completedTests++;
                int best = bestScores.getOrDefault(testId, 0);
                totalScoreSum += best;
                totalMaxScore += test.getQuestions().size();
            }
        }

        TextView tvAttempts = findViewById(R.id.tvAttempts);
        TextView tvSuccess = findViewById(R.id.tvSuccessPercent);
        TextView tvCompletedTests = findViewById(R.id.tvCompletedTests);

        int totalAttempts = attempts.values().stream().mapToInt(Integer::intValue).sum();
        tvAttempts.setText(String.valueOf(totalAttempts));

        int overallPercent = totalMaxScore == 0 ? 0 : (totalScoreSum * 100 / totalMaxScore);
        tvSuccess.setText(overallPercent + "%");

        tvCompletedTests.setText(completedTests + "/" + totalTests);

        ProgressBar progressOverall = findViewById(R.id.progressOverall);
        progressOverall.setProgress(overallPercent);

        List<Lecture> lectures = DataProvider.getLectures();
        Map<Integer, Boolean> readMap = progressManager.getLecturesRead();
        int readCount = 0;
        for (Lecture l : lectures) {
            if (readMap.getOrDefault(l.getId(), false)) readCount++;
        }
        TextView tvTheoryProgress = findViewById(R.id.tvTheoryProgress);
        tvTheoryProgress.setText("Прогресс: " + readCount + "/" + lectures.size() + " лекций");

        List<PracticeScenario> scenarios = DataProvider.getPracticeScenarios();
        Map<Integer, Boolean> completedMap = progressManager.getScenariosCompleted();
        int completedScenarios = 0;
        for (PracticeScenario s : scenarios) {
            if (completedMap.getOrDefault(s.getId(), false)) completedScenarios++;
        }
        TextView tvPracticeProgress = findViewById(R.id.tvPracticeProgress);
        tvPracticeProgress.setText("Прогресс: " + completedScenarios + "/" + scenarios.size() + " сценариев");

        double averageScore = 0;
        if (completedTests > 0) {
            averageScore = (double) totalScoreSum / completedTests;
        }
        TextView tvTestsProgress = findViewById(R.id.tvTestsProgress);
        tvTestsProgress.setText(String.format("Средний балл: %.1f", averageScore) + " • Пройдено: " + completedTests + "/" + totalTests);

        List<String> topErrors = progressManager.getTopErrors(3);
        TextView tvErrorAnalysis = findViewById(R.id.tvErrorAnalysis);
        if (topErrors.isEmpty()) {
            tvErrorAnalysis.setText("Пока нет ошибок. Продолжайте в том же духе!");
        } else {
            StringBuilder errorsText = new StringBuilder();
            for (int i = 0; i < topErrors.size(); i++) {
                errorsText.append(i + 1).append(". ").append(topErrors.get(i)).append("\n\n");
            }
            tvErrorAnalysis.setText(errorsText.toString());
        }

        List<String> recommendations = progressManager.getRecommendations(3);
        TextView tvRecommendations = findViewById(R.id.tvRecommendations);
        if (recommendations.isEmpty()) {
            tvRecommendations.setText("Отличная работа! Вы прошли почти всё.");
        } else {
            StringBuilder recText = new StringBuilder();
            for (int i = 0; i < recommendations.size(); i++) {
                recText.append("• ").append(recommendations.get(i)).append("\n\n");
            }
            tvRecommendations.setText(recText.toString());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}