package vn.poly.personalmanagement.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.database.table.InfoTable;
import vn.poly.personalmanagement.model.DetailExercise;
import vn.poly.personalmanagement.model.Fitness;
import vn.poly.personalmanagement.model.Income;
import vn.poly.personalmanagement.model.ObjectDate;

public class IncomesDAO {

    private Mydatabase myDatabase;
    private SQLiteDatabase db;

    public IncomesDAO(Mydatabase myDatabase) {
        this.myDatabase = myDatabase;
    }

    public long deleteData(Income income) {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_IMCOMES, InfoTable.COL_INCOME_ID + " = ?",
                new String[]{String.valueOf(income.getId())});
    }

    public long deleteDataWithDate(String date) {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_IMCOMES, InfoTable.COL_INCOME_DATE + " = ?", new String[]{date});
    }

    public long addData(Income income) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_INCOME_TITLE, income.getTitle());
        values.put(InfoTable.COL_INCOME_DATE, income.getDate());
        values.put(InfoTable.COL_INCOME_TIME, income.getTime());
        values.put(InfoTable.COL_INCOME_AMOUNT, income.getAmount());
        values.put(InfoTable.COL_INCOME_DESCRIPTION, income.getDescription());
        long query = db.insert(InfoTable.TABLE_IMCOMES, null, values);
        if (query > 0) {
            return query;
        } else return 0;
    }

    public long updateData(Income income) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_INCOME_TITLE, income.getTitle());
        values.put(InfoTable.COL_INCOME_DATE, income.getDate());
        values.put(InfoTable.COL_INCOME_TIME, income.getTime());
        values.put(InfoTable.COL_INCOME_AMOUNT, income.getAmount());
        values.put(InfoTable.COL_INCOME_DESCRIPTION, income.getDescription());
        long query = db.update(InfoTable.TABLE_IMCOMES, values, InfoTable.COL_INCOME_ID + " = ?",
                new String[]{String.valueOf(income.getId())});
        if (query > 0) {
            return query;
        } else return 0;

    }

    public long updateIncomeDate(String oldDate, String newDate) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_INCOME_DATE, newDate);
        long query = db.update(InfoTable.TABLE_IMCOMES, values, InfoTable.COL_INCOME_DATE + " = ?",
                new String[]{oldDate});
        if (query > 0) {
            return query;
        } else return 0;

    }

    public List<Income> getAllIncomeWithDate(String date) {
        db = myDatabase.getWritableDatabase();
        List<Income> incomeList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_IMCOMES + "" +
                " WHERE " + InfoTable.COL_INCOME_DATE + " ='" + date + "'";
        //     + " ORDER BY " + InfoTable.COL_DETAIL_EXERCISE_DATE;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int income_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_INCOME_ID));
                String income_title = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_TITLE));
                String income_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_DATE));
                String income_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_TIME));
                double income_amount = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_INCOME_AMOUNT));
                String income_description = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_DESCRIPTION));

                Income income = new Income(income_id, income_title, income_date, income_time, income_amount, income_description);
                incomeList.add(income);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return incomeList;
    }

    public List<Income> getAllIncomes() {
        db = myDatabase.getWritableDatabase();
        List<Income> incomeList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_IMCOMES+ " ORDER BY " + InfoTable.COL_INCOME_DATE;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int income_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_INCOME_ID));
                String income_title = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_TITLE));
                String income_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_DATE));
                String income_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_TIME));
                double income_amount = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_INCOME_AMOUNT));
                String income_description = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_DESCRIPTION));

                Income income = new Income(income_id, income_title, income_date, income_time, income_amount, income_description);
                incomeList.add(income);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return incomeList;
    }

    public List<Income> getResultSearchedWithDate(String date) {
        db = myDatabase.getWritableDatabase();
        List<Income> incomeList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_IMCOMES
                + " WHERE " + InfoTable.COL_INCOME_DATE + " LIKE '" + date + "%' "
                + "ORDER BY " + InfoTable.COL_INCOME_DATE;

        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int income_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_INCOME_ID));
                String income_title = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_TITLE));
                String income_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_DATE));
                String income_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_TIME));
                double income_amount = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_INCOME_AMOUNT));
                String income_description = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_DESCRIPTION));

                Income income = new Income(income_id, income_title, income_date, income_time, income_amount, income_description);
                incomeList.add(income);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return incomeList;
    }

    public List<ObjectDate> getIncomeDateList() {
        db = myDatabase.getWritableDatabase();
        List<ObjectDate> objectDateList = new ArrayList<>();
        String getData = "SELECT " + InfoTable.COL_INCOME_DATE
                + ", COUNT(" + InfoTable.COL_INCOME_ID + ") AS '" + InfoTable.COL_AMOUNT + "'"
                + " FROM " + InfoTable.TABLE_IMCOMES
                + " GROUP BY " + InfoTable.COL_INCOME_DATE;

        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_DATE));
                int amount = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_AMOUNT));
                ObjectDate objectDate = new ObjectDate(date,amount);
                objectDateList.add(objectDate);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Collections.reverse(objectDateList);
        return objectDateList;
    }


//    public Meal getMeal(int mealID) {
//        db = myDatabase.getWritableDatabase();
//        String getData = "SELECT * FROM " + InfoTable.TABLE_MEALS + " WHERE " + InfoTable.COL_MEAL_ID + " = " + mealID + "";
//        Cursor cursor = db.rawQuery(getData, null);
//        if (cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            int meal_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_MEAL_ID));
//            String meal_title = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_TITLE));
//            String meal_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_TIME));
//            String meal_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DATE));
//            String meal_detail = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DETAIL));
//            Meal meal = new Meal(meal_id, meal_title, meal_time, meal_date, meal_detail);
//            cursor.close();
//            return meal;
//        }
//        return null;
//    }
//    public List<Eating> getEatingList() {
//        db = myDatabase.getWritableDatabase();
//        List<Eating> eatingList = new ArrayList<>();
//        String getData = "SELECT " + InfoTable.COL_MEAL_ID + "," + InfoTable.COL_MEAL_DATE + ", COUNT(" + InfoTable.COL_MEAL_ID + ") AS '" + InfoTable.COL_EATING_AMOUNT_MEAL + "'"
//                + " FROM " + InfoTable.TABLE_MEALS
//                + " GROUP BY " + InfoTable.COL_MEAL_DATE;
//
//        Cursor cursor = db.rawQuery(getData, null);
//        if (cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                int eating_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_MEAL_ID));
//                String eating_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DATE));
//                int amount_meal = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_EATING_AMOUNT_MEAL));
//
//                Eating eating = new Eating(eating_id, eating_date, amount_meal);
//                eatingList.add(eating);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        }
//        Collections.reverse(eatingList);
//        return eatingList;
//    }
//
//    public List<Eating> getEatingList(String date) {
//        db = myDatabase.getWritableDatabase();
//        List<Eating> eatingList = new ArrayList<>();
//        String getData = "SELECT " + InfoTable.COL_MEAL_ID + "," + InfoTable.COL_MEAL_DATE + ", COUNT(" + InfoTable.COL_MEAL_ID + ") AS '" + InfoTable.COL_EATING_AMOUNT_MEAL + "'"
//
//                + " FROM " + InfoTable.TABLE_MEALS
//                + " WHERE " + InfoTable.COL_MEAL_DATE + " LIKE '" + date + "%'"
//                + " GROUP BY " + InfoTable.COL_MEAL_DATE;
//
//        Cursor cursor = db.rawQuery(getData, null);
//        if (cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                int eating_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_MEAL_ID));
//                String eating_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DATE));
//                int amount_meal = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_EATING_AMOUNT_MEAL));
//
//                Eating eating = new Eating(eating_id, eating_date, amount_meal);
//                eatingList.add(eating);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        }
//        Collections.reverse(eatingList);
//        return eatingList;
//    }

}
