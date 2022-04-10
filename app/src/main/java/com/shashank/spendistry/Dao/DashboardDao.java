package com.shashank.spendistry.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.shashank.spendistry.Models.Dashboard;

@Dao
public interface DashboardDao {

    @Query("SELECT * FROM dashboard WHERE id= :id")
    LiveData<Dashboard> getDashboard(String id);

    @Insert
    void addDashboardData(Dashboard dashboard);

    @Query("DELETE FROM dashboard")
    void deleteAll();
}
