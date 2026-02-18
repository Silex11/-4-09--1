package com.example.automatedsystemssimulator.managers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.automatedsystemssimulator.R;
import com.example.automatedsystemssimulator.data.Test;
import java.util.List;

public class TestManager extends RecyclerView.Adapter<TestManager.ViewHolder> {

    private List<Test> tests;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Test test);
    }

    public TestManager(List<Test> tests, OnItemClickListener listener) {
        this.tests = tests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Test test = tests.get(position);
        holder.tvTitle.setText(test.getTitle());
        holder.tvTopic.setText(test.getTopic());
        holder.tvQuestionsCount.setText(test.getQuestions().size() + " вопросов");
        holder.cardView.setOnClickListener(v -> listener.onItemClick(test));
    }

    @Override
    public int getItemCount() { return tests.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitle, tvTopic, tvQuestionsCount;

        ViewHolder(View v) {
            super(v);
            cardView = (CardView) v;
            tvTitle = v.findViewById(R.id.tvTitle);
            tvTopic = v.findViewById(R.id.tvTopic);
            tvQuestionsCount = v.findViewById(R.id.tvQuestionsCount);
        }
    }
}