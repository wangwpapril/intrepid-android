package com.swishlabs.intrepid_android.data.store;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.swishlabs.intrepid_android.MyApplication;
import com.swishlabs.intrepid_android.util.StringUtil;

import java.util.ArrayList;

public class Database {
    private String name;

    //trips constants
    public static final String TABLE_TRIPS = "trips";
    public static final String TABLE_HEALTH_CONDITION = "healthCondition";
    public static final String TABLE_HEALTH_MEDICATION = "healthMedication";

    public static final String KEY_ID = "id";
    public static final String KEY_COUNTRY_ID = "countryId";
    public static final String KEY_GENERAL_IMAGE_URI = "imageGeneral";
    public static final String KEY_DESTINATION_COUNTRY = "destinationCountry";

    public static final String KEY_CONDITION_ID = "healthConditionId";
    public static final String KEY_CONDITION_NAME = "healthConditionName";
    public static final String KEY_CONDITION_DESCRIPTION = "healthConditionDescription";
    public static final String KEY_CONDITION_SYMPTOMS = "healthConditionSymptoms";
    public static final String KEY_CONDITION_PREVENTION = "healthConditionPrevention";

    public static final String KEY_MEDICATION_ID = "healthMedicationId";
    public static final String KEY_MEDICATION_NAME = "healthMedicationName";
    public static final String KEY_MEDICATION_DESCRIPTION = "healthMedicationDescription";
    public static final String KEY_MEDICATION_BRAND_NAME = "healthMedicationBrandName";
    public static final String KEY_MEDICATION_SIDE_EFFECTS = "healthMedicationSideEffects";
    public static final String KEY_MEDICATION_STORAGE = "healthMedicationStorage";
    public static final String KEY_MEDICATION_NOTES = "healthMedicationNotes";

    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private class DatabaseOpenHelper extends SQLiteOpenHelper {
        Context mContext;
        DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
            super(context, name, cursorFactory, version);
            mContext = context;
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String createTripsTable = "CREATE TABLE " + TABLE_TRIPS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DESTINATION_COUNTRY + " TEXT," + KEY_GENERAL_IMAGE_URI + " TEXT,"
                    + KEY_COUNTRY_ID + " TEXT"+ ")";
            db = sqLiteDatabase;
            db.execSQL(createTripsTable);

            String createHealthConditionTable = "CREATE TABLE " + TABLE_HEALTH_CONDITION + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_CONDITION_ID + " TEXT,"
                    + KEY_CONDITION_NAME + " TEXT,"
                    + KEY_COUNTRY_ID + " TEXT,"
                    + KEY_GENERAL_IMAGE_URI + " TEXT,"
                    + KEY_CONDITION_DESCRIPTION + " TEXT,"
                    + KEY_CONDITION_SYMPTOMS + " TEXT,"
                    + KEY_CONDITION_PREVENTION + " TEXT"+ ")";
            db.execSQL(createHealthConditionTable);

            String createHealthMedicationTable = "CREATE TABLE " + TABLE_HEALTH_MEDICATION + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_MEDICATION_ID + " TEXT,"
                    + KEY_MEDICATION_NAME + " TEXT,"
                    + KEY_COUNTRY_ID + " TEXT,"
                    + KEY_GENERAL_IMAGE_URI + " TEXT,"
                    + KEY_MEDICATION_BRAND_NAME + " TEXT,"
                    + KEY_MEDICATION_DESCRIPTION + " TEXT,"
                    + KEY_MEDICATION_SIDE_EFFECTS + " TEXT,"
                    + KEY_MEDICATION_STORAGE + " TEXT,"
                    + KEY_MEDICATION_NOTES + " TEXT"+ ")";
            db.execSQL(createHealthMedicationTable);

        }
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            // delete the database first
            if (newVersion > oldVersion) {
                String sql = "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name";
                Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{});
                ArrayList<String> tables = null;
                if (null != cursor) {
                    try {
                        tables = new ArrayList<String>();
                        while (cursor.moveToNext()) {
                            tables.add(cursor.getString(0));
                        }
                    } catch (Exception e) {
                    } finally {
                        cursor.close();
                    }
                }
                if (null != tables && tables.size() > 0) {
                    for (String table : tables) {
                        sql = "DROP TABLE IF EXISTS " + table;
                        sqLiteDatabase.execSQL(sql);
                    }
                }
            }
        }
    }
    public Database(String name) {
        this(MyApplication.getInstance(), name);
    }
    public Database(Context context, String name) {
        dbOpenHelper = new DatabaseOpenHelper(context, name, null, MyApplication.getInstance().getVersionCode());
        db = dbOpenHelper.getWritableDatabase();
        this.name = name;
    }
    public SQLiteDatabase getDb() {
        return db;
    }
    public synchronized boolean execSql(String sql) {
        boolean ret = false;

        try {
            db.execSQL(sql);
            ret = true;
        } catch (SQLException e) {
 //           Logg.e(e);
        }

        return ret;
    }
    public synchronized boolean execSql(String sql, Object... args) {
        boolean ret = false;

        try {
            if (args == null)
                args = new Object[0];
            db.execSQL(sql, args);
            ret = true;
        } catch (SQLException e) {
 //           Logg.e(e);
        }

        return ret;
    }
    public synchronized Object[][] query(String sql) {
        return query(sql, new String[]{});
    }
    public synchronized Object[][] query(String sql, String[] args) {
        Object[][] ret = null;

        Cursor cursor = null;
        try {
            if (args == null)
                args = new String[]{};
            cursor = db.rawQuery(sql, args);
            if (cursor != null) {
                int columnCount = cursor.getColumnCount();
                ret = new Object[cursor.getCount()][columnCount];
                int row = 0;
                while (cursor.moveToNext()) {
                    for (int i = 0; i < columnCount; i++) {
                        ret[row][i] = cursor.getString(i);
                    }
                    row += 1;
                }
            }
        } catch (Exception e) {
//            Logg.e(e);
        } finally {
            if (cursor != null) cursor.close();
        }

        return ret;
    }
    public Object getSingleValue(String sql, String... args) {
        Object[][] qs = query(sql, args);
        Object ret = null;
        if (qs.length > 0 && qs[0].length > 0)
            ret = qs[0][0];
        return ret;
    }
    public String getSingleString(String sql, String... args) {
        Object q = getSingleValue(sql, args);
        if (q != null)
            return q.toString();
        else
            return "";
    }
    public long count(String tableName) {
        Object q = getSingleValue(StringUtil.simpleFormat("select count(1) from %s;", tableName));
        if (q != null)
            return Long.valueOf(q.toString());
        else
            return 0;
    }
}
