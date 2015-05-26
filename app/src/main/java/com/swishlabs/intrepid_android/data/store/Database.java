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
    public static final String TABLE_DESTINATION_INFORMATION = "destinationInformation";
    public static final String TABLE_EMBASSY = "embassy";
    public static final String TABLE_CURRENCY = "currencyTb";
    public static final String TABLE_ALERT = "alert";

    public static final String KEY_ID = "id";
    public static final String KEY_COUNTRY_ID = "countryId";
    public static final String KEY_COUNTRY_NAME = "countryName";
    public static final String KEY_COUNTRY_CODE = "countryCode";
    public static final String KEY_CURRENCY_CODE = "currencyCode";
    public static final String KEY_CURRENCY_RATE = "currencyRate";
    public static final String KEY_GENERAL_IMAGE_URI = "imageGeneral";
    public static final String KEY_DESTINATION_COUNTRY = "destinationCountry";
    public static final String KEY_TRIP_USER_ID = "userId";

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

    public static final String KEY_DESTINATION_ID = "destinationId";
    public static final String KEY_COMMUNICATIONS = "communicationsInfrastructure";
    public static final String KEY_OTHER_CONCERNS = "otherConcerns";
    public static final String KEY_DEVELOPMENT = "development";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_CULTURAL_NORMS = "culturalNorms";
    public static final String KEY_SOURCES = "sources";
    public static final String KEY_CURRENCY = "currency";
    public static final String KEY_RELIGION = "religion";
    public static final String KEY_TIMEZONE = "timeZone";
    public static final String KEY_SAFETY = "safety";
    public static final String KEY_GOVERNMENT = "government";
    public static final String KEY_VISAMAP = "visaMap";
    public static final String KEY_ELECTRICITY = "electricity";
    public static final String KEY_ETHNIC_MAKEUP = "ethnicMakeup";
    public static final String KEY_LANGUAGE_INFORMATION = "languageInfo";
    public static final String KEY_VISA_REQUIREMENT = "visaRequirement";
    public static final String KEY_CLIMATE_INFO = "climate";
    public static final String KEY_IMAGE_SECURITY = "image1";
    public static final String KEY_IMAGE_OVERVIEW = "image2";
    public static final String KEY_IMAGE_CULTURE = "image3";
    public static final String KEY_IMAGE_INTRO = "image4";
    public static final String KEY_IMAGE_CURRENCY = "image5";
    public static final String KEY_TRANSPORTATION = "transportation";
    public static final String KEY_HOLIDAYS = "holdays";
    public static final String KEY_EMERGENCY_NUMBER = "emergencynumber";
    public static final String KEY_HEALTH_CARE = "healthcarequality";
    public static final String KEY_VACCINATION = "vaccinationspretripmedical";
    public static final String KEY_HEALTH_CONDITION = "healthconditions";
    public static final String KEY_IMAGE_MEDICAL = "medicalimage";

    public static final String KEY_EMBASSY_ID = "embassyId";
    public static final String KEY_EMBASSY_COUNTRY = "embassyCountry";
    public static final String KEY_EMBASSY_NAME = "embassyName";
    public static final String KEY_EMBASSY_SERVICES_OFFERED = "embassyServicesOffered";
    public static final String KEY_EMBASSY_FAX = "embassyFax";
    public static final String KEY_EMBASSY_SOURCE = "embassySource";
    public static final String KEY_EMBASSY_WEBSITE = "embassyWebsite";
    public static final String KEY_EMBASSY_EMAIL = "embassyEmail";
    public static final String KEY_EMBASSY_ADDRESS = "embassyAddress";
    public static final String KEY_EMBASSY_HOURS_OF_OPERATION = "embassyHoursOfOperation";
    public static final String KEY_EMBASSY_NOTES = "embassyNotes";
    public static final String KEY_EMBASSY_TELEPHONE = "embassyTelephone";
    public static final String KEY_EMBASSY_DESTINATION_ID = "embassyDestinationId";
    public static final String KEY_EMBASSY_IMAGE = "embassyImage";

    public static final String KEY_ALERT_CATEGORY = "alertCategory";
    public static final String KEY_ALERT_DESCRIPTION = "alertDescription";
    public static final String KEY_ALERT_STARTDATE = "alertStartDate";
    public static final String KEY_ALERT_ENDDATE = "alertEndDate";



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
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_DESTINATION_COUNTRY + " TEXT,"
                    + KEY_GENERAL_IMAGE_URI + " TEXT,"
                    + KEY_TRIP_USER_ID + " TEXT,"
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

            String createDestinationInfoTable = "CREATE TABLE " + TABLE_DESTINATION_INFORMATION + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_DESTINATION_ID + " TEXT,"
                    + KEY_COUNTRY_NAME + " TEXT,"
                    + KEY_COUNTRY_CODE + " TEXT,"
                    + KEY_COMMUNICATIONS + " TEXT,"
                    + KEY_OTHER_CONCERNS + " TEXT,"
                    + KEY_DEVELOPMENT + " TEXT,"
                    + KEY_LOCATION + " TEXT,"
                    + KEY_CULTURAL_NORMS + " TEXT,"
                    + KEY_SOURCES + " TEXT,"
                    + KEY_CURRENCY + " TEXT,"
                    + KEY_CURRENCY_CODE + " TEXT,"
                    + KEY_CURRENCY_RATE + " TEXT,"
                    + KEY_RELIGION + " TEXT,"
                    + KEY_TIMEZONE + " TEXT,"
                    + KEY_SAFETY + " TEXT,"
                    + KEY_GOVERNMENT + " TEXT,"
                    + KEY_VISAMAP + " TEXT,"
                    + KEY_ELECTRICITY + " TEXT,"
                    + KEY_ETHNIC_MAKEUP + " TEXT,"
                    + KEY_LANGUAGE_INFORMATION + " TEXT,"
                    + KEY_VISA_REQUIREMENT + " TEXT,"
                    + KEY_CLIMATE_INFO + " TEXT,"
                    + KEY_IMAGE_SECURITY + " TEXT,"
                    + KEY_IMAGE_OVERVIEW + " TEXT,"
                    + KEY_IMAGE_CULTURE + " TEXT,"
                    + KEY_IMAGE_INTRO + " TEXT,"
                    + KEY_IMAGE_CURRENCY + " TEXT,"
                    + KEY_EMERGENCY_NUMBER + " TEXT,"
                    + KEY_HEALTH_CARE + " TEXT,"
                    + KEY_VACCINATION + " TEXT,"
                    + KEY_HEALTH_CONDITION + " TEXT,"
                    + KEY_IMAGE_MEDICAL + " TEXT,"
                    + KEY_TRANSPORTATION + " TEXT,"
                    + KEY_HOLIDAYS + " TEXT"+ ")";
            db.execSQL(createDestinationInfoTable);

            String createEmbassyTable = "CREATE TABLE " + TABLE_EMBASSY + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_EMBASSY_ID + " TEXT,"
                    + KEY_EMBASSY_COUNTRY + " TEXT,"
                    + KEY_EMBASSY_NAME + " TEXT,"
                    + KEY_EMBASSY_SERVICES_OFFERED + " TEXT,"
                    + KEY_EMBASSY_FAX + " TEXT,"
                    + KEY_EMBASSY_SOURCE + " TEXT,"
                    + KEY_EMBASSY_WEBSITE + " TEXT,"
                    + KEY_EMBASSY_EMAIL + " TEXT,"
                    + KEY_EMBASSY_ADDRESS + " TEXT,"
                    + KEY_EMBASSY_HOURS_OF_OPERATION + " TEXT,"
                    + KEY_EMBASSY_NOTES + " TEXT,"
                    + KEY_EMBASSY_TELEPHONE + " TEXT,"
                    + KEY_EMBASSY_DESTINATION_ID + " TEXT,"
                    + KEY_EMBASSY_IMAGE + " TEXT"+ ")";
            db.execSQL(createEmbassyTable);

            String createCurrencyTable = "CREATE TABLE " + TABLE_CURRENCY + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_CURRENCY_CODE + " TEXT,"
                    + KEY_GENERAL_IMAGE_URI + " TEXT"+ ")";
            db.execSQL(createCurrencyTable);

            String createAlertTable = "CREATE TABLE " + TABLE_ALERT + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_COUNTRY_CODE + " TEXT,"
                    + KEY_ALERT_CATEGORY + " TEXT,"
                    + KEY_ALERT_DESCRIPTION + " TEXT,"
                    + KEY_ALERT_STARTDATE + " TEXT,"
                    + KEY_ALERT_ENDDATE + " TEXT"+ ")";
            db.execSQL(createAlertTable);

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
                        if("sqlite_sequence".equals(table)||"android_metadata".equals(table))
                            continue;
                        sql = "DROP TABLE IF EXISTS " + table;
                        sqLiteDatabase.execSQL(sql);
                    }
                }

                onCreate(sqLiteDatabase);
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
