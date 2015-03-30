package com.intrepid.travel.store.beans;

import java.io.ByteArrayOutputStream;

import com.intrepid.travel.store.Bean;
import com.intrepid.travel.utils.StringUtil;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class Image extends Bean {
    private static final String tableName = "image";

    private static Image instance;

    public static Image getInstance() {
        if (instance == null)
            instance = new Image();
        return instance;
    }

    public Image() {
        super();
    }

    @Override
    public void createTable() {
        String sql = StringUtil.simpleFormat("CREATE TABLE if not exists %s (key TEXT PRIMARY KEY, content BLOB, time INTEGER);", tableName);
        db.execSql(sql);
    }

    public void save(String key, Bitmap content) {
        save(key, content, System.currentTimeMillis());
    }

    public void save(String key, Bitmap content, long time) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        content.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String sql = StringUtil.simpleFormat("REPLACE INTO %s (key, content, time) VALUES(?, ?, ?)", tableName);
        db.execSql(sql, key, baos.toByteArray(), time);
    }

    public Bitmap get(String key) {
        String sql = StringUtil.simpleFormat("select content from %s where key=?", tableName);
        Cursor cursor = db.getDb().rawQuery(sql, new String[]{key});
        if (null != cursor) {
            try {
                if (cursor.moveToNext()) {
                    byte[] content = cursor.getBlob(0);
                    if (null != content && content.length > 0)
                        return BitmapFactory.decodeByteArray(content, 0, content.length);
                }
            } catch (Exception e) {
 //               Logg.e(e);
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * remove expired image
     * @param day
     */
    public void removeExpired(int day) {
        long time = System.currentTimeMillis() - day * 24 * 3600000;
        String sql = StringUtil.simpleFormat("delete from %s where time<?", tableName);
        db.execSql(sql, time);
    }

    public void removeAll() {
        String sql = "delete from image";
        db.execSql(sql);
    }
}
