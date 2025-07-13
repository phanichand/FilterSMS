package com.example.filtersms.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filtersms.R;
import com.example.filtersms.data.SmsFilterRule;

import java.util.ArrayList;
import java.util.List;

public class RuleAdapter extends RecyclerView.Adapter<RuleAdapter.RuleViewHolder> {

    private List<SmsFilterRule> rules = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SmsFilterRule rule);
        void onDeleteClick(SmsFilterRule rule);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rule_item, parent, false);
        return new RuleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RuleViewHolder holder, int position) {
        SmsFilterRule currentRule = rules.get(position);
        holder.textViewSender.setText("Sender: " + currentRule.getSender());
        holder.textViewMessagePattern.setText("Message: " + currentRule.getMessagePattern());
    }

    @Override
    public int getItemCount() {
        return rules.size();
    }

    public void setRules(List<SmsFilterRule> rules) {
        this.rules = rules;
        notifyDataSetChanged();
    }

    class RuleViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewSender;
        private TextView textViewMessagePattern;
        private TextView textViewDelete;

        public RuleViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSender = itemView.findViewById(R.id.textViewSender);
            textViewMessagePattern = itemView.findViewById(R.id.textViewMessagePattern);
            textViewDelete = itemView.findViewById(R.id.textViewDelete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(rules.get(position));
                    }
                }
            });

            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(rules.get(position));
                    }
                }
            });
        }
    }
}