package vn.poly.personalmanagement.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.database.table.InfoTable;
import vn.poly.personalmanagement.methodclass.CurrentDateTime;
import vn.poly.personalmanagement.model.ObjectDate;
import vn.poly.personalmanagement.model.Plan;

public class PlansDAO {

    private MyDatabase myDatabase;
    private SQLiteDatabase db;

    public PlansDAO(MyDatabase myDatabase) {
        this.myDatabase = myDatabase;
    }

    public long clearAllData() {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_PLANS, null, null);
    }

    public long deleteData(Plan plan) {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_PLANS, InfoTable.COL_PLAN_ID + " = ?",
                new String[]{String.valueOf(plan.getId())});
    }

    public long deleteDataWithDate(String date) {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_PLANS, InfoTable.COL_PLAN_DATE + " = ?", new String[]{date});
    }

    public long addData(Plan plan) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_PLAN_NAME, plan.getPlanName());
        values.put(InfoTable.COL_PLAN_DATE, plan.getDate());
        values.put(InfoTable.COL_PLAN_TIME, plan.getTime());
        values.put(InfoTable.COL_PLAN_IS_ALARMED, plan.getAlarmed());
        values.put(InfoTable.COL_PLAN_DESCRIPTION, plan.getDescription());

        long query = db.insert(InfoTable.TABLE_PLANS, null, values);
        if (query > 0) {
            return query;
        } else return 0;
    }

    public long updateData(Plan plan) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_PLAN_NAME, plan.getPlanName());
        values.put(InfoTable.COL_PLAN_DATE, plan.getDate());
        values.put(InfoTable.COL_PLAN_TIME, plan.getTime());
        values.put(InfoTable.COL_PLAN_IS_ALARMED, plan.getAlarmed());
        values.put(InfoTable.COL_PLAN_DESCRIPTION, plan.getDescription());
        long query = db.update(InfoTable.TABLE_PLANS, values, InfoTable.COL_PLAN_ID + " = ?",
                new String[]{String.valueOf(plan.getId())});
        if (query > 0) {
            return query;
        } else return 0;

    }

    public List<Plan> getAllPlansWithDate(String date) {
        db = myDatabase.getWritableDatabase();
        List<Plan> planList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_PLANS + "" +
                " WHERE " + InfoTable.COL_PLAN_DATE + " ='" + date + "'"
                + " ORDER BY " + InfoTable.COL_PLAN_TIME;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int plan_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_PLAN_ID));
                String plan_name = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_NAME));
                String plan_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_DATE));
                String plan_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_TIME));
                int plan_isAlarm = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_PLAN_IS_ALARMED));
                String plan_description = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_DESCRIPTION));

                Plan plan = new Plan(plan_id, plan_name, plan_date, plan_time, plan_isAlarm, plan_description);
                planList.add(plan);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return planList;
    }

    public List<Plan> getAllPlansFuture() {
        String currentDate = CurrentDateTime.getCurrentDate();
        String currentTime = CurrentDateTime.getCurrentTime();
        db = myDatabase.getWritableDatabase();
        List<Plan> planList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_PLANS
                + " WHERE (" + InfoTable.COL_PLAN_DATE + " >'" + currentDate + "') "
                + " OR (" + InfoTable.COL_PLAN_DATE + " = '" + currentDate + "'" + " AND " + InfoTable.COL_PLAN_TIME + " > '" + currentTime + "')"
                + " ORDER BY " + InfoTable.COL_PLAN_DATE + ", " + InfoTable.COL_PLAN_TIME;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int plan_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_PLAN_ID));
                String plan_name = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_NAME));
                String plan_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_DATE));
                String plan_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_TIME));
                int plan_isAlarm = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_PLAN_IS_ALARMED));
                String plan_description = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_DESCRIPTION));

                Plan plan = new Plan(plan_id, plan_name, plan_date, plan_time, plan_isAlarm, plan_description);
                planList.add(plan);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return planList;
    }

    public List<Plan> getPlansFutureSearched(String name) {
        String currentDate = CurrentDateTime.getCurrentDate();
        String currentTime = CurrentDateTime.getCurrentTime();
        db = myDatabase.getWritableDatabase();
        List<Plan> planList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_PLANS
                + " WHERE ((" + InfoTable.COL_PLAN_DATE + " >'" + currentDate + "') "
                + " OR (" + InfoTable.COL_PLAN_DATE + " = '" + currentDate + "'" + " AND " + InfoTable.COL_PLAN_TIME + " > '" + currentTime + "')) "
                + " AND " + InfoTable.COL_PLAN_NAME + " LIKE '" + name + "%'"
                + " ORDER BY " + InfoTable.COL_PLAN_DATE + ", " + InfoTable.COL_PLAN_TIME;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int plan_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_PLAN_ID));
                String plan_name = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_NAME));
                String plan_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_DATE));
                String plan_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_TIME));
                int plan_isAlarm = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_PLAN_IS_ALARMED));
                String plan_description = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_DESCRIPTION));

                Plan plan = new Plan(plan_id, plan_name, plan_date, plan_time, plan_isAlarm, plan_description);
                planList.add(plan);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return planList;
    }

    public List<Plan> getPlansTodaySearched(String name) {
        String currentDate = CurrentDateTime.getCurrentDate();
        String currentTime = CurrentDateTime.getCurrentTime();
        db = myDatabase.getWritableDatabase();
        List<Plan> planList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_PLANS
                + " WHERE " + InfoTable.COL_PLAN_DATE + " ='" + currentDate + "' "
                + " AND " + InfoTable.COL_PLAN_NAME + " LIKE '" + name + "%'"
                + " ORDER BY " + InfoTable.COL_PLAN_DATE + ", " + InfoTable.COL_PLAN_TIME;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int plan_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_PLAN_ID));
                String plan_name = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_NAME));
                String plan_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_DATE));
                String plan_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_TIME));
                int plan_isAlarm = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_PLAN_IS_ALARMED));
                String plan_description = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_DESCRIPTION));

                Plan plan = new Plan(plan_id, plan_name, plan_date, plan_time, plan_isAlarm, plan_description);
                planList.add(plan);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return planList;
    }

    public List<ObjectDate> getAllPlanDate() {
        String currentDate = CurrentDateTime.getCurrentDate();
        db = myDatabase.getWritableDatabase();
        List<ObjectDate> objectDateList = new ArrayList<>();
        String getData = "SELECT " + InfoTable.COL_PLAN_DATE
                + ", COUNT(" + InfoTable.COL_PLAN_ID + ") AS '" + InfoTable.COL_AMOUNT + "'"
                + " FROM " + InfoTable.TABLE_PLANS
                + " WHERE " + InfoTable.COL_PLAN_DATE + " <'" + currentDate + "'"
                + " GROUP BY " + InfoTable.COL_PLAN_DATE
                + " ORDER BY " + InfoTable.COL_PLAN_DATE;

        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_DATE));
                int amount = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_AMOUNT));
                ObjectDate objectDate = new ObjectDate(date, amount);
                objectDateList.add(objectDate);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Collections.reverse(objectDateList);
        return objectDateList;
    }

    public List<ObjectDate> getAllPlanDate(String date) {
        String currentDate = CurrentDateTime.getCurrentDate();
        db = myDatabase.getWritableDatabase();
        List<ObjectDate> objectDateList = new ArrayList<>();
        String getData = "SELECT " + InfoTable.COL_PLAN_DATE
                + ", COUNT(" + InfoTable.COL_PLAN_ID + ") AS '" + InfoTable.COL_AMOUNT + "'"
                + " FROM " + InfoTable.TABLE_PLANS
                + " WHERE " + InfoTable.COL_PLAN_DATE + " LIKE '" + date + "%'"
                + " GROUP BY " + InfoTable.COL_PLAN_DATE
                + " ORDER BY " + InfoTable.COL_PLAN_DATE;

        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String sDate = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_DATE));
                int amount = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_AMOUNT));
                ObjectDate objectDate = new ObjectDate(sDate, amount);
                objectDateList.add(objectDate);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Collections.reverse(objectDateList);
        return objectDateList;
    }


    public List<Plan> getAllData() {
        db = myDatabase.getWritableDatabase();
        List<Plan> planList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_PLANS;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int plan_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_PLAN_ID));
                String plan_name = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_NAME));
                String plan_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_DATE));
                String plan_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_TIME));
                int plan_isAlarm = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_PLAN_IS_ALARMED));
                String plan_description = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PLAN_DESCRIPTION));

                Plan plan = new Plan(plan_id, plan_name, plan_date, plan_time, plan_isAlarm, plan_description);
                planList.add(plan);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return planList;
    }

}
