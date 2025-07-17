package com.example.filtersms.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LogEntryDao {
    @Insert
    void insert(LogEntry logEntry);

    @Query("SELECT * FROM log_table ORDER BY timestamp DESC")
    List<LogEntry> getAllLogs();

    @Query("DELETE FROM log_table")
    void deleteAllLogs();
}