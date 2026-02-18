package com.example.automatedsystemssimulator.managers;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.automatedsystemssimulator.R;
import com.example.automatedsystemssimulator.data.Achievement;
import java.util.List;

public class AchievementManager extends RecyclerView.Adapter<AchievementManager.ViewHolder> {

    private List<Achievement> achievements;

    public AchievementManager(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Achievement ach = achievements.get(position);
        holder.tvTitle.setText(ach.getTitle());
        holder.ivIcon.setText(ach.getEmoji());

        Context context = holder.itemView.getContext();

        if (ach.isUnlocked()) {
            holder.ivIcon.setTextColor(context.getColor(R.color.black));
            holder.ivIcon.setAlpha(1.0f);
            holder.cardView.setAlpha(1.0f);
        } else {
            holder.ivIcon.setTextColor(context.getColor(R.color.gray_500));
            holder.ivIcon.setAlpha(0.6f);
            holder.cardView.setAlpha(0.8f);
        }

        holder.itemView.setOnClickListener(v -> {
            String status = ach.isUnlocked() ? "✅ Получено" : "❌ Ещё не получено";
            new AlertDialog.Builder(context)
                    .setTitle("Достижение: " + ach.getTitle())
                    .setMessage(ach.getDescription() + "\n\nСтатус: " + status)
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView ivIcon;
        TextView tvTitle;

        ViewHolder(View v) {
            super(v);
            cardView = (CardView) v;
            ivIcon = v.findViewById(R.id.ivIcon);
            tvTitle = v.findViewById(R.id.tvTitle);
        }
    }
}