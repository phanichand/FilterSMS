package com.example.filtersms.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sms_filter_rules")
public class SmsFilterRule {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String sender;
    public String messagePattern;

    public SmsFilterRule(String sender, String messagePattern) {
        this.sender = sender;
        this.messagePattern = messagePattern;
    }

    // Getters (Room uses them for data retrieval)
    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getMessagePattern() {
        return messagePattern;
    }
}