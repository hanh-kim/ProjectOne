package vn.poly.personalmanagement.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.database.table.InfoTable;
import vn.poly.personalmanagement.model.Eating;
import vn.poly.personalmanagement.model.Meal;

public class MealsDAO {
    private MyDatabase myDatabase;
    private SQLiteDatabase db;

    public MealsDAO(MyDatabase myDatabase) {
        this.myDatabase = myDatabase;
    }

    public long clearAllData() {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_MEALS, null, null);
    }

    public long deleteData(Meal meal) {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_MEALS, InfoTable.COL_MEAL_ID + " = ?", new String[]{String.valueOf(meal.getId())});
    }

    public long deleteDataWithDate(String date) {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_MEALS, InfoTable.COL_MEAL_DATE + " = ?", new String[]{date});
    }

    public long addData(Meal meal) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_MEAL_TITLE, meal.getTitle());
        values.put(InfoTable.COL_MEAL_TIME, meal.getTime());
        values.put(InfoTable.COL_MEAL_DATE, meal.getDate());
        values.put(InfoTable.COL_MEAL_DETAIL, meal.getDetailMeal());

        long query = db.insert(InfoTable.TABLE_MEALS, null, values);
        if (query > 0) {
            return query;
        } else return 0;
    }


    public long updateData(Meal meal) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_MEAL_TITLE, meal.getTitle());
        values.put(InfoTable.COL_MEAL_TIME, meal.getTime());
        values.put(InfoTable.COL_MEAL_DATE, meal.getDate());
        values.put(InfoTable.COL_MEAL_DETAIL, meal.getDetailMeal());
        long query = db.update(InfoTable.TABLE_MEALS, values, InfoTable.COL_MEAL_ID + " = ?",
                new String[]{String.valueOf(meal.getId())});
        if (query > 0) {
            return query;
        } else return 0;

    }

    public long updateMealDate(String oldDate, String newDate) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_MEAL_DATE, newDate);
        long query = db.update(InfoTable.TABLE_MEALS, values, InfoTable.COL_MEAL_DATE + " = ?",
                new String[]{oldDate});
        if (query > 0) {
            return query;
        } else return 0;

    }

    public List<Meal> getAllMealWithDate(String date) {
        db = myDatabase.getWritableDatabase();
        List<Meal> mealList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_MEALS + " WHERE " + InfoTable.COL_MEAL_DATE + " ='" + date + "'"
                + "ORDER BY " + InfoTable.COL_MEAL_DATE;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int meal_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_MEAL_ID));
                String meal_title = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_TITLE));
                String meal_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_TIME));
                String meal_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DATE));
                String meal_detail = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DETAIL));
                Meal meal = new Meal(meal_id, meal_title, meal_date, meal_time, meal_detail);
                mealList.add(meal);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return mealList;
    }

    public Meal getMeal(int mealID) {
        db = myDatabase.getWritableDatabase();
        String getData = "SELECT * FROM " + InfoTable.TABLE_MEALS + " WHERE " + InfoTable.COL_MEAL_ID + " = " + mealID + "";
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int meal_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_MEAL_ID));
            String meal_title = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_TITLE));
            String meal_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_TIME));
            String meal_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DATE));
            String meal_detail = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DETAIL));
            Meal meal = new Meal(meal_id, meal_title, meal_time, meal_date, meal_detail);
            cursor.close();
            return meal;
        }
        return null;
    }

    public List<Meal> getResultSearchedWithDate(String date) {
        db = myDatabase.getWritableDatabase();
        List<Meal> mealList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_MEALS + " WHERE " + InfoTable.COL_MEAL_DATE + " LIKE '" + date + "%' "
                + "ORDER BY " + InfoTable.COL_MEAL_DATE;

        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int meal_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_MEAL_ID));
                String meal_title = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_TITLE));
                String meal_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_TIME));
                String meal_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DATE));
                String meal_detail = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DETAIL));
                Meal meal = new Meal(meal_id, meal_title, meal_date, meal_time, meal_detail);
                mealList.add(meal);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return mealList;
    }

    public List<Eating> getEatingList() {
        db = myDatabase.getWritableDatabase();
        List<Eating> eatingList = new ArrayList<>();
        String getData = "SELECT " + InfoTable.COL_MEAL_ID + "," + InfoTable.COL_MEAL_DATE + ", COUNT(" + InfoTable.COL_MEAL_ID + ") AS '" + InfoTable.COL_EATING_AMOUNT_MEAL + "'"
                + " FROM " + InfoTable.TABLE_MEALS
                + " GROUP BY " + InfoTable.COL_MEAL_DATE;

        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int eating_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_MEAL_ID));
                String eating_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DATE));
                int amount_meal = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_EATING_AMOUNT_MEAL));

                Eating eating = new Eating(eating_id, eating_date, amount_meal);
                eatingList.add(eating);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Collections.reverse(eatingList);
        return eatingList;
    }

    public List<Eating> getEatingList(String date) {
        db = myDatabase.getWritableDatabase();
        List<Eating> eatingList = new ArrayList<>();
        String getData = "SELECT " + InfoTable.COL_MEAL_ID + "," + InfoTable.COL_MEAL_DATE + ", COUNT(" + InfoTable.COL_MEAL_ID + ") AS '" + InfoTable.COL_EATING_AMOUNT_MEAL + "'"
                + " FROM " + InfoTable.TABLE_MEALS
                + " WHERE " + InfoTable.COL_MEAL_DATE + " LIKE '" + date + "%'"
                + " GROUP BY " + InfoTable.COL_MEAL_DATE;

        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int eating_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_MEAL_ID));
                String eating_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DATE));
                int amount_meal = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_EATING_AMOUNT_MEAL));

                Eating eating = new Eating(eating_id, eating_date, amount_meal);
                eatingList.add(eating);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Collections.reverse(eatingList);
        return eatingList;
    }


    public List<Meal> getAllData() {
        db = myDatabase.getWritableDatabase();
        List<Meal> mealList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_MEALS ;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int meal_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_MEAL_ID));
                String meal_title = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_TITLE));
                String meal_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_TIME));
                String meal_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DATE));
                String meal_detail = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DETAIL));
                Meal meal = new Meal(meal_id, meal_title, meal_date, meal_time, meal_detail);
                mealList.add(meal);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return mealList;
    }
}

