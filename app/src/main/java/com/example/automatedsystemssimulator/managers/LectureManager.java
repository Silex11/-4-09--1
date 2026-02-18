package com.example.automatedsystemssimulator.managers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.automatedsystemssimulator.R;
import com.example.automatedsystemssimulator.data.Lecture;
import java.util.List;

public class LectureManager extends RecyclerView.Adapter<LectureManager.ViewHolder> {

    private List<Lecture> lectures;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Lecture lecture);
    }

    public LectureManager(List<Lecture> lectures, OnItemClickListener listener) {
        this.lectures = lectures;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lecture, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lecture lecture = lectures.get(position);
        holder.tvTitle.setText(lecture.getTitle());
        holder.tvTopic.setText(lecture.getTopic());
        holder.ivIcon.setImageResource(lecture.getIconRes());
        holder.cardView.setOnClickListener(v -> listener.onItemClick(lecture));
    }

    @Override
    public int getItemCount() { return lectures.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivIcon;
        TextView tvTitle, tvTopic;

        ViewHolder(View v) {
            super(v);
            cardView = (CardView) v;
            ivIcon = v.findViewById(R.id.ivIcon);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvTopic = v.findViewById(R.id.tvTopic);
        }
    }
}