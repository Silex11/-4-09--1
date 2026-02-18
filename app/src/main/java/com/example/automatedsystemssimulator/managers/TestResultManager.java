package com.example.automatedsystemssimulator.managers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.automatedsystemssimulator.R;
import com.example.automatedsystemssimulator.data.Question;
import java.util.List;

public class TestResultManager extends RecyclerView.Adapter<TestResultManager.ViewHolder> {

    private List<Question> questions;
    private int[] userAnswers;
    private int[] correctIndices;

    public TestResultManager(List<Question> questions, int[] userAnswers, int[] correctIndices) {
        this.questions = questions;
        this.userAnswers = userAnswers;
        this.correctIndices = correctIndices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test_result, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question q = questions.get(position);
        holder.tvQuestionText.setText(String.format("%d. %s", position + 1, q.getText()));

        int userAns = userAnswers[position];
        int correctAns = correctIndices[position];

        String userAnswerText;
        if (userAns == -1) {
            userAnswerText = "Не выбран";
        } else if (userAns >= 0 && userAns < q.getOptions().size()) {
            userAnswerText = q.getOptions().get(userAns).getText();
        } else {
            userAnswerText = "Ошибка: индекс " + userAns;
        }

        String correctAnswerText;
        if (correctAns >= 0 && correctAns < q.getOptions().size()) {
            correctAnswerText = q.getOptions().get(correctAns).getText();
        } else {
            correctAnswerText = "Ошибка: индекс " + correctAns;
        }

        holder.tvUserAnswer.setText("Ваш ответ: " + userAnswerText);
        holder.tvCorrectAnswer.setText("Правильный ответ: " + correctAnswerText);

        if (userAns == correctAns && userAns != -1 && userAns < q.getOptions().size()) {
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.light_green_50));
        } else {
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getColor(R.color.light_red_50));
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvQuestionText, tvUserAnswer, tvCorrectAnswer;

        ViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.cardView);
            tvQuestionText = v.findViewById(R.id.tvQuestionText);
            tvUserAnswer = v.findViewById(R.id.tvUserAnswer);
            tvCorrectAnswer = v.findViewById(R.id.tvCorrectAnswer);
        }
    }
}