package com.example.filtersms.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sms_filter_rules")
public class SmsFilterRule {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String sender;
    public String messagePattern;

    public boolean sendToGoogleSheet;
    public String sheetId;
    public String sheetName;

    public boolean sendToWebhook;
    public String webhookUrl;

    public SmsFilterRule(String sender, String messagePattern, boolean sendToGoogleSheet, String sheetId, String sheetName, boolean sendToWebhook, String webhookUrl) {
        this.sender = sender;
        this.messagePattern = messagePattern;
        this.sendToGoogleSheet = sendToGoogleSheet;
        this.sheetId = sheetId;
        this.sheetName = sheetName;
        this.sendToWebhook = sendToWebhook;
        this.webhookUrl = webhookUrl;
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

    public boolean isSendToGoogleSheet() {
        return sendToGoogleSheet;
    }

    public String getSheetId() {
        return sheetId;
    }

    public String getSheetName() {
        return sheetName;
    }

    public boolean isSendToWebhook() {
        return sendToWebhook;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setId(int id) {
        this.id = id;
    }
}