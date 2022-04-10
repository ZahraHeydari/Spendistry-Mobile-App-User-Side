package com.shashank.spendistry.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.shashank.spendistry.Converters.Converters;
import com.shashank.spendistry.Dao.DashboardDao;
import com.shashank.spendistry.Models.Dashboard;

@Database(entities = {Dashboard.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class SpendistryDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "SpendistryDB";
    public abstract DashboardDao dashboardDao();
    private static volatile SpendistryDatabase INSTANCE;

    public static SpendistryDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SpendistryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE= Room.databaseBuilder(context, SpendistryDatabase.class, DATABASE_NAME).addCallback(callback).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
    static RoomDatabase.Callback callback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new AddItemsAsyncTask(INSTANCE);
        }
    };




    static class AddItemsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final DashboardDao dashboardDao;


        AddItemsAsyncTask(SpendistryDatabase spendistryDatabase) {
            dashboardDao = spendistryDatabase.dashboardDao();
            Log.i("Test", "PopulateAsyncTask: test");

        }

        @Override
        protected Void doInBackground(Void... voids) {
            dashboardDao.deleteAll();
            Log.w("main123", "doInBackground: bg working");
            return null;
        }
    }
}
