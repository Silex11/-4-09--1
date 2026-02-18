package com.example.automatedsystemssimulator.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.automatedsystemssimulator.data.DataProvider;
import com.example.automatedsystemssimulator.data.Question;
import com.example.automatedsystemssimulator.data.Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ProgressController {
    private static final String PREFS_NAME = "user_progress";
    private static final String KEY_TEST_ATTEMPTS = "test_attempts";
    private static final String KEY_TEST_BEST_SCORES = "test_best_scores";
    private static final String KEY_SCENARIO_COMPLETED = "scenario_completed";
    private static final String KEY_LECTURE_READ = "lecture_read";
    private static final String KEY_LAST_TEST_RESULT_PREFIX = "last_test_result_";
    private static final String KEY_LAST_PRACTICE_RESULT_PREFIX = "last_practice_result_";
    private static final String KEY_ACHIEVEMENTS = "achievements";

    // Константы достижений
    public static final String ACH_FIRST_LECTURE = "first_lecture";
    public static final String ACH_FIVE_LECTURES = "five_lectures";
    public static final String ACH_ALL_LECTURES = "all_lectures";
    public static final String ACH_FIRST_PRACTICE = "first_practice";
    public static final String ACH_FIVE_PRACTICES = "five_practices";
    public static final String ACH_ALL_PRACTICES = "all_practices";
    public static final String ACH_FIRST_TEST = "first_test";
    public static final String ACH_FIVE_TESTS = "five_tests";
    public static final String ACH_ALL_TESTS = "all_tests";
    public static final String ACH_PERFECT_TEST = "perfect_test";
    public static final String ACH_FIRST_ATTEMPT = "first_attempt";
    public static final String ACH_TEN_ATTEMPTS = "ten_attempts";
    public static final String ACH_ALL_COMPLETED = "all_completed";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public ProgressController(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveTestAttempt(int testId, int score, int maxScore) {
        Map<Integer, Integer> attempts = getTestAttempts();
        attempts.put(testId, attempts.getOrDefault(testId, 0) + 1);
        saveIntMap(KEY_TEST_ATTEMPTS, attempts);

        Map<Integer, Integer> bestScores = getTestBestScores();
        int currentBest = bestScores.getOrDefault(testId, 0);
        if (score > currentBest) {
            bestScores.put(testId, score);
            saveIntMap(KEY_TEST_BEST_SCORES, bestScores);
        }
    }

    public Map<Integer, Integer> getTestAttempts() {
        return loadIntMap(KEY_TEST_ATTEMPTS);
    }

    public Map<Integer, Integer> getTestBestScores() {
        return loadIntMap(KEY_TEST_BEST_SCORES);
    }

    public int getTestBestPercent(int testId, int maxScore) {
        Map<Integer, Integer> best = getTestBestScores();
        Integer score = best.get(testId);
        if (score == null) return 0;
        return (int) (score * 100.0 / maxScore);
    }

    public void saveLastTestResult(int testId, int correct, int total, int[] userAnswers, int[] correctIndices) {
        JSONObject json = new JSONObject();
        try {
            json.put("correct", correct);
            json.put("total", total);

            org.json.JSONArray userArr = new org.json.JSONArray();
            for (int a : userAnswers) userArr.put(a);
            json.put("userAnswers", userArr);

            org.json.JSONArray correctArr = new org.json.JSONArray();
            for (int c : correctIndices) correctArr.put(c);
            json.put("correctIndices", correctArr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString(KEY_LAST_TEST_RESULT_PREFIX + testId, json.toString());
        editor.apply();
    }

    public Bundle getLastTestResult(int testId) {
        String jsonString = prefs.getString(KEY_LAST_TEST_RESULT_PREFIX + testId, null);
        if (jsonString == null) return null;
        try {
            JSONObject json = new JSONObject(jsonString);
            Bundle bundle = new Bundle();
            bundle.putInt("correct", json.getInt("correct"));
            bundle.putInt("total", json.getInt("total"));

            org.json.JSONArray userArr = json.getJSONArray("userAnswers");
            int[] userAnswers = new int[userArr.length()];
            for (int i = 0; i < userArr.length(); i++) userAnswers[i] = userArr.getInt(i);
            bundle.putIntArray("userAnswers", userAnswers);

            org.json.JSONArray correctArr = json.getJSONArray("correctIndices");
            int[] correctIndices = new int[correctArr.length()];
            for (int i = 0; i < correctArr.length(); i++) correctIndices[i] = correctArr.getInt(i);
            bundle.putIntArray("correctIndices", correctIndices);

            return bundle;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveLastPracticeResult(int scenarioId, int selectedOptionIndex, boolean isCorrect, String feedback) {
        JSONObject json = new JSONObject();
        try {
            json.put("selectedOption", selectedOptionIndex);
            json.put("isCorrect", isCorrect);
            json.put("feedback", feedback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString(KEY_LAST_PRACTICE_RESULT_PREFIX + scenarioId, json.toString());
        editor.apply();
    }

    public Bundle getLastPracticeResult(int scenarioId) {
        String jsonString = prefs.getString(KEY_LAST_PRACTICE_RESULT_PREFIX + scenarioId, null);
        if (jsonString == null) return null;
        try {
            JSONObject json = new JSONObject(jsonString);
            Bundle bundle = new Bundle();
            bundle.putInt("selectedOption", json.getInt("selectedOption"));
            bundle.putBoolean("isCorrect", json.getBoolean("isCorrect"));
            bundle.putString("feedback", json.getString("feedback"));
            return bundle;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void clearLastPracticeResult(int scenarioId) {
        editor.remove(KEY_LAST_PRACTICE_RESULT_PREFIX + scenarioId);
        editor.apply();
    }

    public void markScenarioCompleted(int scenarioId) {
        Map<Integer, Boolean> completed = getScenariosCompleted();
        completed.put(scenarioId, true);
        saveBooleanMap(KEY_SCENARIO_COMPLETED, completed);
    }

    public Map<Integer, Boolean> getScenariosCompleted() {
        return loadBooleanMap(KEY_SCENARIO_COMPLETED);
    }

    public boolean isScenarioCompleted(int scenarioId) {
        return getScenariosCompleted().getOrDefault(scenarioId, false);
    }

    public void markLectureRead(int lectureId) {
        Map<Integer, Boolean> read = getLecturesRead();
        read.put(lectureId, true);
        saveBooleanMap(KEY_LECTURE_READ, read);
    }

    public Map<Integer, Boolean> getLecturesRead() {
        return loadBooleanMap(KEY_LECTURE_READ);
    }

    public boolean isLectureRead(int lectureId) {
        return getLecturesRead().getOrDefault(lectureId, false);
    }

    private void saveIntMap(String key, Map<Integer, Integer> map) {
        JSONObject json = new JSONObject();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            try {
                json.put(String.valueOf(entry.getKey()), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString(key, json.toString());
        editor.apply();
    }

    private Map<Integer, Integer> loadIntMap(String key) {
        String jsonString = prefs.getString(key, "{}");
        Map<Integer, Integer> map = new HashMap<>();
        try {
            JSONObject json = new JSONObject(jsonString);
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String strKey = keys.next();
                int intKey = Integer.parseInt(strKey);
                int value = json.getInt(strKey);
                map.put(intKey, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    private void saveBooleanMap(String key, Map<Integer, Boolean> map) {
        JSONObject json = new JSONObject();
        for (Map.Entry<Integer, Boolean> entry : map.entrySet()) {
            try {
                json.put(String.valueOf(entry.getKey()), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString(key, json.toString());
        editor.apply();
    }

    private Map<Integer, Boolean> loadBooleanMap(String key) {
        String jsonString = prefs.getString(key, "{}");
        Map<Integer, Boolean> map = new HashMap<>();
        try {
            JSONObject json = new JSONObject(jsonString);
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String strKey = keys.next();
                int intKey = Integer.parseInt(strKey);
                boolean value = json.getBoolean(strKey);
                map.put(intKey, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String, Boolean> getAchievements() {
        String jsonString = prefs.getString(KEY_ACHIEVEMENTS, "{}");
        Map<String, Boolean> map = new HashMap<>();
        try {
            JSONObject json = new JSONObject(jsonString);
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                map.put(key, json.getBoolean(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addMissingAchievements(map);
        return map;
    }

    private void addMissingAchievements(Map<String, Boolean> map) {
        String[] allAchievements = {
                ACH_FIRST_LECTURE, ACH_FIVE_LECTURES, ACH_ALL_LECTURES,
                ACH_FIRST_PRACTICE, ACH_FIVE_PRACTICES, ACH_ALL_PRACTICES,
                ACH_FIRST_TEST, ACH_FIVE_TESTS, ACH_ALL_TESTS,
                ACH_PERFECT_TEST, ACH_FIRST_ATTEMPT, ACH_TEN_ATTEMPTS,
                ACH_ALL_COMPLETED
        };
        for (String ach : allAchievements) {
            if (!map.containsKey(ach)) {
                map.put(ach, false);
            }
        }
    }

    public void saveAchievements(Map<String, Boolean> achievements) {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, Boolean> entry : achievements.entrySet()) {
            try {
                json.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString(KEY_ACHIEVEMENTS, json.toString());
        editor.apply();
    }

    public void checkAndUnlockAchievements() {
        Map<String, Boolean> achievements = getAchievements();
        boolean changed = false;

        int lecturesRead = getLecturesRead().size();
        int totalLectures = DataProvider.getLectures().size();
        int practicesCompleted = getScenariosCompleted().size();
        int totalPractices = DataProvider.getPracticeScenarios().size();
        Map<Integer, Integer> testAttempts = getTestAttempts();
        Map<Integer, Integer> bestScores = getTestBestScores();
        int testsPassed = testAttempts.size();
        int totalTests = DataProvider.getTests().size();

        int totalAttempts = 0;
        for (int count : testAttempts.values()) {
            totalAttempts += count;
        }

        if (!achievements.get(ACH_FIRST_LECTURE) && lecturesRead >= 1) {
            achievements.put(ACH_FIRST_LECTURE, true);
            changed = true;
        }
        if (!achievements.get(ACH_FIVE_LECTURES) && lecturesRead >= 5) {
            achievements.put(ACH_FIVE_LECTURES, true);
            changed = true;
        }
        if (!achievements.get(ACH_ALL_LECTURES) && lecturesRead >= totalLectures) {
            achievements.put(ACH_ALL_LECTURES, true);
            changed = true;
        }

        if (!achievements.get(ACH_FIRST_PRACTICE) && practicesCompleted >= 1) {
            achievements.put(ACH_FIRST_PRACTICE, true);
            changed = true;
        }
        if (!achievements.get(ACH_FIVE_PRACTICES) && practicesCompleted >= 5) {
            achievements.put(ACH_FIVE_PRACTICES, true);
            changed = true;
        }
        if (!achievements.get(ACH_ALL_PRACTICES) && practicesCompleted >= totalPractices) {
            achievements.put(ACH_ALL_PRACTICES, true);
            changed = true;
        }

        if (!achievements.get(ACH_FIRST_TEST) && testsPassed >= 1) {
            achievements.put(ACH_FIRST_TEST, true);
            changed = true;
        }
        if (!achievements.get(ACH_FIVE_TESTS) && testsPassed >= 5) {
            achievements.put(ACH_FIVE_TESTS, true);
            changed = true;
        }
        if (!achievements.get(ACH_ALL_TESTS) && testsPassed >= totalTests) {
            achievements.put(ACH_ALL_TESTS, true);
            changed = true;
        }

        boolean perfectExists = false;
        for (Map.Entry<Integer, Integer> entry : bestScores.entrySet()) {
            Test test = DataProvider.getTestById(entry.getKey());
            if (test != null && entry.getValue() == test.getQuestions().size()) {
                perfectExists = true;
                break;
            }
        }
        if (!achievements.get(ACH_PERFECT_TEST) && perfectExists) {
            achievements.put(ACH_PERFECT_TEST, true);
            changed = true;
        }

        if (!achievements.get(ACH_FIRST_ATTEMPT) && totalAttempts >= 1) {
            achievements.put(ACH_FIRST_ATTEMPT, true);
            changed = true;
        }
        if (!achievements.get(ACH_TEN_ATTEMPTS) && totalAttempts >= 10) {
            achievements.put(ACH_TEN_ATTEMPTS, true);
            changed = true;
        }

        if (!achievements.get(ACH_ALL_COMPLETED) &&
                lecturesRead >= totalLectures &&
                practicesCompleted >= totalPractices &&
                testsPassed >= totalTests) {
            achievements.put(ACH_ALL_COMPLETED, true);
            changed = true;
        }

        if (changed) {
            saveAchievements(achievements);
        }
    }

    public Map<Integer, Bundle> getAllLastTestResults() {
        Map<Integer, Bundle> results = new HashMap<>();
        List<Test> tests = DataProvider.getTests();
        for (Test test : tests) {
            Bundle result = getLastTestResult(test.getId());
            if (result != null) {
                results.put(test.getId(), result);
            }
        }
        return results;
    }

    public List<String> getTopErrors(int limit) {
        Map<String, Integer> errorCountByTopic = new HashMap<>();
        Map<Integer, Bundle> results = getAllLastTestResults();

        for (Map.Entry<Integer, Bundle> entry : results.entrySet()) {
            int testId = entry.getKey();
            Bundle result = entry.getValue();
            int[] userAnswers = result.getIntArray("userAnswers");
            int[] correctIndices = result.getIntArray("correctIndices");
            Test test = DataProvider.getTestById(testId);
            if (test == null) continue;
            String topic = test.getTopic();
            List<Question> questions = test.getQuestions();

            for (int i = 0; i < userAnswers.length; i++) {
                if (userAnswers[i] != correctIndices[i]) {
                    String errorKey = topic + ": " + questions.get(i).getText();
                    errorCountByTopic.put(errorKey, errorCountByTopic.getOrDefault(errorKey, 0) + 1);
                }
            }
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(errorCountByTopic.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<String> topErrors = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, list.size()); i++) {
            Map.Entry<String, Integer> e = list.get(i);
            topErrors.add(e.getKey() + " (" + e.getValue() + " раз)");
        }
        return topErrors;
    }

    public List<String> getRecommendations(int limit) {
        List<String> recommendations = new ArrayList<>();

        int lecturesRead = getLecturesRead().size();
        int totalLectures = DataProvider.getLectures().size();
        int practicesCompleted = getScenariosCompleted().size();
        int totalPractices = DataProvider.getPracticeScenarios().size();
        Map<Integer, Integer> testAttempts = getTestAttempts();
        int testsPassed = testAttempts.size();
        int totalTests = DataProvider.getTests().size();

        if (lecturesRead < totalLectures) {
            recommendations.add("Изучите непрочитанные лекции: осталось " + (totalLectures - lecturesRead));
        }
        if (practicesCompleted < totalPractices) {
            recommendations.add("Выполните сценарии: осталось " + (totalPractices - practicesCompleted));
        }
        if (testsPassed < totalTests) {
            recommendations.add("Пройдите тесты: осталось " + (totalTests - testsPassed));
        }

        if (recommendations.size() > limit) {
            recommendations = recommendations.subList(0, limit);
        }
        return recommendations;
    }

    public int getOverallProgress() {
        int lecturesRead = getLecturesRead().size();
        int totalLectures = DataProvider.getLectures().size();
        int practicesCompleted = getScenariosCompleted().size();
        int totalPractices = DataProvider.getPracticeScenarios().size();
        Map<Integer, Integer> testAttempts = getTestAttempts();
        int testsPassed = testAttempts.size();
        int totalTests = DataProvider.getTests().size();

        int lecturesPercent = totalLectures == 0 ? 0 : (lecturesRead * 100 / totalLectures);
        int practicesPercent = totalPractices == 0 ? 0 : (practicesCompleted * 100 / totalPractices);
        int testsPercent = totalTests == 0 ? 0 : (testsPassed * 100 / totalTests);

        return (lecturesPercent + practicesPercent + testsPercent) / 3;
    }

    public String getSkillLevel() {
        int progress = getOverallProgress();
        if (progress < 30) return "Начинающий";
        if (progress < 70) return "Средний";
        return "Эксперт";
    }

    public List<String> getNextGoals() {
        List<String> goals = new ArrayList<>();

        int lecturesRead = getLecturesRead().size();
        int totalLectures = DataProvider.getLectures().size();
        int practicesCompleted = getScenariosCompleted().size();
        int totalPractices = DataProvider.getPracticeScenarios().size();
        Map<Integer, Integer> testAttempts = getTestAttempts();
        int testsPassed = testAttempts.size();
        int totalTests = DataProvider.getTests().size();

        if (lecturesRead < totalLectures) {
            goals.add("Прочитать лекции: осталось " + (totalLectures - lecturesRead));
        }
        if (practicesCompleted < totalPractices) {
            goals.add("Выполнить сценарии: осталось " + (totalPractices - practicesCompleted));
        }
        if (testsPassed < totalTests) {
            goals.add("Пройдите тесты: осталось " + (totalTests - testsPassed));
        }

        if (goals.isEmpty()) {
            Map<String, Boolean> achievements = getAchievements();
            if (!achievements.get(ACH_PERFECT_TEST)) {
                goals.add("Получите 100% в любом тесте");
            }
            if (!achievements.get(ACH_TEN_ATTEMPTS)) {
                goals.add("Сделайте 10 попыток тестов");
            }
        }

        return goals;
    }
}