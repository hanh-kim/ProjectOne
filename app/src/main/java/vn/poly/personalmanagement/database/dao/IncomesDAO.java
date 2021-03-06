package vn.poly.personalmanagement.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.database.table.InfoTable;
import vn.poly.personalmanagement.model.Income;
import vn.poly.personalmanagement.model.ObjectDate;

public class IncomesDAO {

    private MyDatabase myDatabase;
    private SQLiteDatabase db;

    public IncomesDAO(MyDatabase myDatabase) {
        this.myDatabase = myDatabase;
    }

    public long clearAllData() {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_INCOMES, null, null);
    }

    public long deleteData(Income income) {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_INCOMES, InfoTable.COL_INCOME_ID + " = ?",
                new String[]{String.valueOf(income.getId())});
    }

    public long deleteDataWithDate(String date) {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_INCOMES, InfoTable.COL_INCOME_DATE + " = ?", new String[]{date});
    }

    public long addData(Income income) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_INCOME_TITLE, income.getTitle());
        values.put(InfoTable.COL_INCOME_DATE, income.getDate());
        values.put(InfoTable.COL_INCOME_TIME, income.getTime());
        values.put(InfoTable.COL_INCOME_AMOUNT, income.getAmount());
        values.put(InfoTable.COL_INCOME_DESCRIPTION, income.getDescription());
        long query = db.insert(InfoTable.TABLE_INCOMES, null, values);
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
        long query = db.update(InfoTable.TABLE_INCOMES, values, InfoTable.COL_INCOME_ID + " = ?",
                new String[]{String.valueOf(income.getId())});
        if (query > 0) {
            return query;
        } else return 0;

    }

    public List<Income> getAllIncomes() {
        db = myDatabase.getWritableDatabase();
        List<Income> incomeList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_INCOMES + " ORDER BY " + InfoTable.COL_INCOME_DATE;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int income_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_INCOME_ID));
                String income_title = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_TITLE));
                String income_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_DATE));
                String income_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_TIME));
                long income_amount = cursor.getLong(cursor.getColumnIndex(InfoTable.COL_INCOME_AMOUNT));
//                String income_description = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_DESCRIPTION));
                String income_description = cursor.getString(5);
                Income income = new Income(income_id, income_title, income_date, income_time, income_amount, income_description);
                incomeList.add(income);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return incomeList;
    }

    public List<Income> getResultSearched(String str) {
        db = myDatabase.getWritableDatabase();
        List<Income> incomeList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_INCOMES
                + " WHERE (" + InfoTable.COL_INCOME_DATE + " LIKE '" + str + "%' )"
                + " OR (" + InfoTable.COL_INCOME_TITLE + " LIKE '" + str + "%' )"
                + "ORDER BY " + InfoTable.COL_INCOME_DATE;

        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int income_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_INCOME_ID));
                String income_title = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_TITLE));
                String income_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_DATE));
                String income_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_TIME));
                long income_amount = cursor.getLong(cursor.getColumnIndex(InfoTable.COL_INCOME_AMOUNT));
//                String income_description = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_DESCRIPTION));
                String income_description = cursor.getString(5);
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
                + " FROM " + InfoTable.TABLE_INCOMES
                + " GROUP BY " + InfoTable.COL_INCOME_DATE;

        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_DATE));
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

    public List<Income> getAllIncomes(String from, String to) {
        db = myDatabase.getWritableDatabase();
        List<Income> incomeList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_INCOMES
                + " WHERE " + InfoTable.COL_INCOME_DATE + " BETWEEN '" + from + "' AND '" + to + "'";
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int income_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_INCOME_ID));
                String income_title = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_TITLE));
                String income_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_DATE));
                String income_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_TIME));
                long income_amount = cursor.getLong(cursor.getColumnIndex(InfoTable.COL_INCOME_AMOUNT));
//                String income_description = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_DESCRIPTION));
                String income_description = cursor.getString(5);
                Income income = new Income(income_id, income_title, income_date, income_time, income_amount, income_description);
                incomeList.add(income);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return incomeList;
    }

    public List<Income> getAllIncomeWithDate(String date) {
        db = myDatabase.getWritableDatabase();
        List<Income> incomeList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_INCOMES + "" +
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
                long income_amount = cursor.getLong(cursor.getColumnIndex(InfoTable.COL_INCOME_AMOUNT));
//                String income_description = cursor.getString(cursor.getColumnIndex(InfoTable.COL_INCOME_DESCRIPTION));
                String income_description = cursor.getString(5);
                Income income = new Income(income_id, income_title, income_date, income_time, income_amount, income_description);
                incomeList.add(income);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return incomeList;
    }



}
