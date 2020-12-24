package com.hhe.androidjetpackroom.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.hhe.androidjetpackroom.db.dao.UserDao;
import com.hhe.androidjetpackroom.db.entity.User;

@Database(entities = {User.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();


    public static final String DATABASE_NAME = "database-name";
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();
    private static AppDatabase sInstance;
    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext(), executors);
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext,
                                             final AppExecutors executors) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
//                        executors.diskIO().execute(() -> {
//                            // Generate the data for pre-population
//                            AppDatabase database = AppDatabase.getInstance(appContext, executors);
//                            // notify that the database was created and it's ready to be used
//                            database.setDatabaseCreated();
//                        });
                    }
                })
                .addMigrations(MIGRATION_1_2)
                .build();
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }


    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE temp_user (" +
                    "uid INTEGER PRIMARY KEY NOT NULL," +
                    "first_name TEXT," +
                    "last_name TEXT)");
            database.execSQL("INSERT INTO temp_user (uid, first_name, last_name) " +
                    "SELECT uid, first_name, last_name FROM user");
            database.execSQL("DROP TABLE user");
            database.execSQL("ALTER TABLE temp_user RENAME TO user");
        }
    };

}
