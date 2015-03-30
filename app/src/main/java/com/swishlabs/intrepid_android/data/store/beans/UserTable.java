package com.swishlabs.intrepid_android.data.store.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import android.database.Cursor;
import android.util.Base64;

import com.swishlabs.intrepid_android.data.api.model.User;
import com.swishlabs.intrepid_android.data.store.Bean;
import com.swishlabs.intrepid_android.util.StringUtil;


public class UserTable extends Bean {

	public static final String TABLE_NAME = "user";

	private static final String USER_ID = "user_id";
    private static final String SAVE_COMP = "save_company";
    

    private static UserTable instance;

    public static UserTable getInstance() {
        if (null == instance) {
            instance = new UserTable();
        }

        return instance;
    }

    @Override
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
        		+ USER_ID + " TEXT, "
                + SAVE_COMP + " TEXT);";
        db.execSql(sql);
    }

    public void saveUser(User user) {

    	if (user != null) {
            String sql = "insert into " + TABLE_NAME
                    + " values(" + user.id                        + ","
                    				                               + ")";
            db.execSql(sql);
        }
        
        
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bao);
            oos.writeObject(user);
            oos.flush();
            oos.close();
            bao.close();

            String dealStream = Base64.encodeToString(bao.toByteArray(), Base64.DEFAULT);
            
            String sql;
            String userId = user.id;
            if (getCount() > 0) {
            	sql = "UPDATE " + TABLE_NAME + " SET " + SAVE_COMP + " = ? "  + " WHERE " + USER_ID  +" = " + userId ;
            	db.execSql(sql, dealStream);
            } else {
            	sql = StringUtil.simpleFormat("replace into %s (%s,%s) values (?,?)", TABLE_NAME, SAVE_COMP, USER_ID);
            	 db.execSql(sql, dealStream,userId);
            }

            
        } catch (Exception e) {
 //           Logg.e(e);
        }
        
    }

        public synchronized User getUser( String userid ) {
            String sql = "SELECT " + SAVE_COMP + " FROM " + TABLE_NAME  + " WHERE " + USER_ID  +" = " + userid;
            Cursor cursor = db.getDb().rawQuery(sql, null);

            return paserUser(cursor);
        }

        private User paserUser(Cursor cursor) {
        	User user = null;
            if (cursor == null || !cursor.moveToFirst()) {
                if (cursor != null) {
                    cursor.close();
                }
                return user;
            }

            ByteArrayInputStream bis = null;
            ObjectInputStream ois = null;
            try {
                String data = cursor.getString(0);
                bis = new ByteArrayInputStream(Base64.decode(data, Base64.DEFAULT));
                ois = new ObjectInputStream(bis);
                Object object = ois.readObject();

                return (User) object;
            } catch (Exception e) {
     //           Logg.e(e);
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
      //              Logg.e(e);
                }
                cursor.close();
            }

            return user;
        }       
        

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
        Cursor cursor = db.getDb().rawQuery(sql, null);
        cursor.moveToFirst();
        int n = cursor.getInt(0);
        cursor.close();
        return n;
    }
    
    public boolean remove() {
        String sql = "DELETE FROM " + TABLE_NAME;
        return db.execSql(sql);
    }

}
