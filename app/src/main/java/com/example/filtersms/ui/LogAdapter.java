package com.example.filtersms.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filtersms.R;
import com.example.filtersms.data.LogEntry;

import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {

    private List<LogEntry> logs = new ArrayList<>();

    public void setLogs(List<LogEntry> logs) {
        this.logs = logs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_log, parent, false);
        return new LogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        LogEntry currentLog = logs.get(position);
        holder.textViewTimestamp.setText(currentLog.getTimestamp());
        holder.textViewMessage.setText(currentLog.getMessage());
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    static class LogViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTimestamp;
        private TextView textViewMessage;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }
}