package com.example.filtersms.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "log_table")
public class LogEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String timestamp;
    public String message;

    public LogEntry(String timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}