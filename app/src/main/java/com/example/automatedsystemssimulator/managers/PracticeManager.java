package com.example.automatedsystemssimulator.managers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.automatedsystemssimulator.R;
import com.example.automatedsystemssimulator.data.PracticeScenario;
import java.util.List;

public class PracticeManager extends RecyclerView.Adapter<PracticeManager.ViewHolder> {

    private List<PracticeScenario> scenarios;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PracticeScenario scenario);
    }

    public PracticeManager(List<PracticeScenario> scenarios, OnItemClickListener listener) {
        this.scenarios = scenarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_practice, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PracticeScenario s = scenarios.get(position);
        holder.tvTitle.setText(s.getTitle());
        holder.tvTopic.setText(s.getTopic());
        String difficulty = s.getDifficulty() == 1 ? "★" : s.getDifficulty() == 2 ? "★★" : "★★★";
        holder.tvDifficulty.setText(difficulty);
        holder.cardView.setOnClickListener(v -> listener.onItemClick(s));
    }

    @Override
    public int getItemCount() { return scenarios.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitle, tvTopic, tvDifficulty;

        ViewHolder(View v) {
            super(v);
            cardView = (CardView) v;
            tvTitle = v.findViewById(R.id.tvTitle);
            tvTopic = v.findViewById(R.id.tvTopic);
            tvDifficulty = v.findViewById(R.id.tvDifficulty);
        }
    }
}