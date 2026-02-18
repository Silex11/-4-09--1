package com.example.automatedsystemssimulator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.automatedsystemssimulator.data.DataProvider;
import com.example.automatedsystemssimulator.data.Option;
import com.example.automatedsystemssimulator.data.PracticeScenario;
import com.example.automatedsystemssimulator.utils.ProgressController;

public class PracticeActivity extends AppCompatActivity {

    private static final String EXTRA_SCENARIO_ID = "scenario_id";

    private PracticeScenario scenario;
    private ProgressController progressManager;
    private TextView tvResult;
    private CardView[] optionCards = new CardView[3];
    private Button btnReset;
    private int scenarioId;

    public static void start(Context context, int scenarioId) {
        Intent intent = new Intent(context, PracticeActivity.class);
        intent.putExtra(EXTRA_SCENARIO_ID, scenarioId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        scenarioId = getIntent().getIntExtra(EXTRA_SCENARIO_ID, -1);
        scenario = DataProvider.getScenarioById(scenarioId);
        if (scenario == null) {
            finish();
            return;
        }

        progressManager = new ProgressController(this);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvTask = findViewById(R.id.tvTask);
        tvResult = findViewById(R.id.txtResult);
        btnReset = findViewById(R.id.btnReset);

        tvTitle.setText(scenario.getTitle());
        tvDescription.setText(scenario.getDescription());
        tvTask.setText(scenario.getTask());

        optionCards[0] = findViewById(R.id.cardOption1);
        optionCards[1] = findViewById(R.id.cardOption2);
        optionCards[2] = findViewById(R.id.cardOption3);

        for (int i = 0; i < scenario.getOptions().size(); i++) {
            Option opt = scenario.getOptions().get(i);
            TextView tvOptionText;
            if (i == 0) tvOptionText = optionCards[i].findViewById(R.id.tvOptionText1);
            else if (i == 1) tvOptionText = optionCards[i].findViewById(R.id.tvOptionText2);
            else tvOptionText = optionCards[i].findViewById(R.id.tvOptionText3);

            tvOptionText.setText(opt.getText());

            final int index = i;
            optionCards[i].setOnClickListener(v -> onOptionSelected(index));
        }

        Bundle lastResult = progressManager.getLastPracticeResult(scenarioId);
        if (lastResult != null) {
            String feedback = lastResult.getString("feedback");
            boolean isCorrect = lastResult.getBoolean("isCorrect");
            tvResult.setText(feedback);
            if (isCorrect) {
                tvResult.setBackgroundColor(getColor(android.R.color.holo_green_light));
            } else {
                tvResult.setBackgroundColor(getColor(android.R.color.holo_red_light));
            }
        } else {
            tvResult.setText("Выберите вариант действия, чтобы увидеть результат...");
            tvResult.setBackgroundColor(getColor(R.color.gray_100));
        }

        btnReset.setOnClickListener(v -> {
            progressManager.clearLastPracticeResult(scenarioId);
            tvResult.setText("Выберите вариант действия, чтобы увидеть результат...");
            tvResult.setBackgroundColor(getColor(R.color.gray_100));
        });
    }

    private void onOptionSelected(int index) {
        Option selected = scenario.getOptions().get(index);
        tvResult.setText(selected.getFeedback());

        if (selected.isCorrect()) {
            tvResult.setBackgroundColor(getColor(android.R.color.holo_green_light));
            if (!progressManager.isScenarioCompleted(scenario.getId())) {
                progressManager.markScenarioCompleted(scenario.getId());
            }
        } else {
            tvResult.setBackgroundColor(getColor(android.R.color.holo_red_light));
        }

        progressManager.saveLastPracticeResult(scenarioId, index, selected.isCorrect(), selected.getFeedback());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}