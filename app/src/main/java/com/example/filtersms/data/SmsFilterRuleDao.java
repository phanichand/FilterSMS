package com.example.filtersms.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SmsFilterRuleDao {
    @Query("SELECT * FROM sms_filter_rules ORDER BY id DESC")
    List<SmsFilterRule> getAllRules();

    @Insert
    void insertRule(SmsFilterRule rule);

    @Update
    void updateRule(SmsFilterRule rule);

    @Delete
    void deleteRule(SmsFilterRule rule);

    @Query("SELECT * FROM sms_filter_rules WHERE id = :ruleId")
    SmsFilterRule getRuleById(int ruleId);
}