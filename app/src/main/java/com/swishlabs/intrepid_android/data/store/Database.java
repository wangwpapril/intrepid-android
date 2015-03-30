package com.intrepid.travel.store;


import java.util.ArrayList;
import com.intrepid.travel.MyApplication;
import com.intrepid.travel.utils.StringUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database {
    private String name;

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
 //                       Logg.e(e);
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
