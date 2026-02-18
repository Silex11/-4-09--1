package com.example.automatedsystemssimulator;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.automatedsystemssimulator.managers.AchievementManager;
import com.example.automatedsystemssimulator.data.Achievement;
import com.example.automatedsystemssimulator.data.DataProvider;
import com.example.automatedsystemssimulator.utils.ProgressController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProgressActivity extends AppCompatActivity {

    private ProgressController progressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Прогресс обучения");
        }

        progressManager = new ProgressController(this);
        progressManager.checkAndUnlockAchievements();

        int overallProgress = progressManager.getOverallProgress();
        com.google.android.material.progressindicator.CircularProgressIndicator circularProgress = findViewById(R.id.circularProgress);
        TextView tvOverallPercent = findViewById(R.id.tvOverallPercent);
        circularProgress.setProgress(overallProgress);
        tvOverallPercent.setText(overallProgress + "%");

        int lecturesRead = progressManager.getLecturesRead().size();
        int totalLectures = DataProvider.getLectures().size();
        int practicesCompleted = progressManager.getScenariosCompleted().size();
        int totalPractices = DataProvider.getPracticeScenarios().size();
        Map<Integer, Integer> testAttempts = progressManager.getTestAttempts();
        int testsPassed = testAttempts.size();
        int totalTests = DataProvider.getTests().size();

        TextView tvLecturesProgress = findViewById(R.id.tvLecturesProgress);
        TextView tvScenariosProgress = findViewById(R.id.tvScenariosProgress);
        TextView tvTestsProgress = findViewById(R.id.tvTestsProgress);

        tvLecturesProgress.setText(lecturesRead + "/" + totalLectures);
        tvScenariosProgress.setText(practicesCompleted + "/" + totalPractices);
        tvTestsProgress.setText(testsPassed + "/" + totalTests);

        String level = progressManager.getSkillLevel();
        TextView tvSkillLevel = findViewById(R.id.tvSkillLevel);
        tvSkillLevel.setText(level);

        RecyclerView recyclerAchievements = findViewById(R.id.recyclerAchievements);
        recyclerAchievements.setLayoutManager(new GridLayoutManager(this, 4)); // 4 в ряд
        List<Achievement> achievementsList = buildAchievementsList();

        AchievementManager adapter = new AchievementManager(achievementsList);
        recyclerAchievements.setAdapter(adapter);

        List<String> goals = progressManager.getNextGoals();
        TextView tvNextGoals = findViewById(R.id.tvNextGoals);
        if (goals.isEmpty()) {
            tvNextGoals.setText("Поздравляем! Вы выполнили все основные цели.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < goals.size(); i++) {
                sb.append(i + 1).append(". ").append(goals.get(i)).append("\n");
            }
            tvNextGoals.setText(sb.toString());
        }
    }

    private List<Achievement> buildAchievementsList() {
        Map<String, Boolean> unlockedMap = progressManager.getAchievements();
        List<Achievement> list = new ArrayList<>();

        list.add(new Achievement(ProgressController.ACH_FIRST_LECTURE, "Первая лекция",
                "Прочитайте любую лекцию", "📘", unlockedMap.get(ProgressController.ACH_FIRST_LECTURE)));
        list.add(new Achievement(ProgressController.ACH_FIVE_LECTURES, "Книгочей",
                "Прочитайте 5 лекций", "📚", unlockedMap.get(ProgressController.ACH_FIVE_LECTURES)));
        list.add(new Achievement(ProgressController.ACH_ALL_LECTURES, "Эрудит",
                "Прочитайте все лекции", "🏛️", unlockedMap.get(ProgressController.ACH_ALL_LECTURES)));
        list.add(new Achievement(ProgressController.ACH_FIRST_PRACTICE, "Первая практика",
                "Выполните любой сценарий", "🔧", unlockedMap.get(ProgressController.ACH_FIRST_PRACTICE)));
        list.add(new Achievement(ProgressController.ACH_FIVE_PRACTICES, "Практик",
                "Выполните 5 сценариев", "⚙️", unlockedMap.get(ProgressController.ACH_FIVE_PRACTICES)));
        list.add(new Achievement(ProgressController.ACH_ALL_PRACTICES, "Мастер",
                "Выполните все сценарии", "🏆", unlockedMap.get(ProgressController.ACH_ALL_PRACTICES)));
        list.add(new Achievement(ProgressController.ACH_FIRST_TEST, "Первый тест",
                "Пройдите любой тест", "📝", unlockedMap.get(ProgressController.ACH_FIRST_TEST)));
        list.add(new Achievement(ProgressController.ACH_FIVE_TESTS, "Тестировщик",
                "Пройдите 5 тестов", "✅", unlockedMap.get(ProgressController.ACH_FIVE_TESTS)));
        list.add(new Achievement(ProgressController.ACH_ALL_TESTS, "Отличник",
                "Пройдите все тесты", "🎓", unlockedMap.get(ProgressController.ACH_ALL_TESTS)));
        list.add(new Achievement(ProgressController.ACH_PERFECT_TEST, "Идеальный",
                "Получите 100% в любом тесте", "🌟", unlockedMap.get(ProgressController.ACH_PERFECT_TEST)));
        list.add(new Achievement(ProgressController.ACH_FIRST_ATTEMPT, "Первая попытка",
                "Сделайте первую попытку теста", "🎯", unlockedMap.get(ProgressController.ACH_FIRST_ATTEMPT)));
        list.add(new Achievement(ProgressController.ACH_TEN_ATTEMPTS, "Упорство",
                "Сделайте 10 попыток тестов", "💪", unlockedMap.get(ProgressController.ACH_TEN_ATTEMPTS)));
        list.add(new Achievement(ProgressController.ACH_ALL_COMPLETED, "Все звёзды",
                "Завершите все лекции, сценарии и тесты", "👑", unlockedMap.get(ProgressController.ACH_ALL_COMPLETED)));

        return list;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}