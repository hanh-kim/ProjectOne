package vn.poly.personalmanagement.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.database.table.InfoTable;
import vn.poly.personalmanagement.model.Expense;
import vn.poly.personalmanagement.model.ObjectDate;

public class ExpensesDAO {

    private MyDatabase myDatabase;
    private SQLiteDatabase db;

    public ExpensesDAO(MyDatabase myDatabase) {
        this.myDatabase = myDatabase;
    }

    public long clearAllData() {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_EXPENSES, null, null);
    }

    public long deleteData(Expense expense) {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_EXPENSES, InfoTable.COL_EXPENSE_ID + " = ?",
                new String[]{String.valueOf(expense.getId())});
    }

    public long deleteDataWithDate(String date) {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_EXPENSES, InfoTable.COL_EXPENSE_DATE + " = ?", new String[]{date});
    }

    public long addData(Expense expense) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_EXPENSE_TITLE, expense.getTitle());
        values.put(InfoTable.COL_EXPENSE_DATE, expense.getDate());
        values.put(InfoTable.COL_EXPENSE_TIME, expense.getTime());
        values.put(InfoTable.COL_EXPENSE_AMOUNT, expense.getAmount());
        values.put(InfoTable.COL_EXPENSE_DESCRIPTION, expense.getDescription());
        long query = db.insert(InfoTable.TABLE_EXPENSES, null, values);
        if (query > 0) {
            return query;
        } else return 0;
    }

    public long saveDataFromFirebase(List<Expense> expenseList) {
        for (Expense expense : expenseList) {
            db = myDatabase.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(InfoTable.COL_EXPENSE_TITLE, expense.getTitle());
            values.put(InfoTable.COL_EXPENSE_DATE, expense.getDate());
            values.put(InfoTable.COL_EXPENSE_TIME, expense.getTime());
            values.put(InfoTable.COL_EXPENSE_AMOUNT, expense.getAmount());
            values.put(InfoTable.COL_EXPENSE_DESCRIPTION, expense.getDescription());
            long query = db.insert(InfoTable.TABLE_EXPENSES, null, values);
            if (query > 0) {
                return query;
            } else return 0;
        }
        return 0;
    }

    public long updateData(Expense expense) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_EXPENSE_TITLE, expense.getTitle());
        values.put(InfoTable.COL_EXPENSE_DATE, expense.getDate());
        values.put(InfoTable.COL_EXPENSE_TIME, expense.getTime());
        values.put(InfoTable.COL_EXPENSE_AMOUNT, expense.getAmount());
        values.put(InfoTable.COL_EXPENSE_DESCRIPTION, expense.getDescription());
        long query = db.update(InfoTable.TABLE_EXPENSES, values, InfoTable.COL_EXPENSE_ID + " = ?",
                new String[]{String.valueOf(expense.getId())});
        if (query > 0) {
            return query;
        } else return 0;

    }

    public List<Expense> getAllExpensesWithDate(String date) {
        db = myDatabase.getWritableDatabase();
        List<Expense> expenseList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_EXPENSES + "" +
                " WHERE " + InfoTable.COL_EXPENSE_DATE + " ='" + date + "'";
        //     + " ORDER BY " + InfoTable.COL_DETAIL_EXERCISE_DATE;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int expense_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_EXPENSE_ID));
                String expense_title = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXPENSE_TITLE));
                String expense_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXPENSE_DATE));
                String expense_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXPENSE_TIME));
                long expense_amount = cursor.getLong(cursor.getColumnIndex(InfoTable.COL_EXPENSE_AMOUNT));
                //   String expense_description = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXPENSE_DESCRIPTION));
                String expense_description = cursor.getString(5);
                Expense expense = new Expense(expense_id, expense_title, expense_date, expense_time, expense_amount, expense_description);
                expenseList.add(expense);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return expenseList;
    }

    public List<Expense> getAllExpenses() {
        db = myDatabase.getWritableDatabase();
        List<Expense> expenseList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_EXPENSES
                + " ORDER BY " + InfoTable.COL_EXPENSE_DATE;

        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int expense_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_EXPENSE_ID));
                String expense_title = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXPENSE_TITLE));
                String expense_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXPENSE_DATE));
                String expense_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXPENSE_TIME));
                long expense_amount = cursor.getLong(cursor.getColumnIndex(InfoTable.COL_EXPENSE_AMOUNT));
//                String expense_description = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXPENSE_DESCRIPTION));
                String expense_description = cursor.getString(5);
                Expense expense = new Expense(expense_id, expense_title, expense_date, expense_time, expense_amount, expense_description);
                expenseList.add(expense);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return expenseList;
    }

    public List<Expense> getAllExpenses(String from, String to) {
        db = myDatabase.getWritableDatabase();
        List<Expense> expenseList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_EXPENSES
                + " WHERE " + InfoTable.COL_EXPENSE_DATE + " BETWEEN '" + from + "' AND '" + to + "'";
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int expense_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_EXPENSE_ID));
                String expense_title = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXPENSE_TITLE));
                String expense_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXPENSE_DATE));
                String expense_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXPENSE_TIME));
                long expense_amount = cursor.getLong(cursor.getColumnIndex(InfoTable.COL_EXPENSE_AMOUNT));
//                String expense_description = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXPENSE_DESCRIPTION));
                String expense_description = cursor.getString(5);
                Expense expense = new Expense(expense_id, expense_title, expense_date, expense_time, expense_amount, expense_description);
                expenseList.add(expense);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return expenseList;
    }

    public List<ObjectDate> getResultSearched(String date) {
        db = myDatabase.getWritableDatabase();
        List<ObjectDate> objectDateList = new ArrayList<>();
        String getData = "SELECT " + InfoTable.COL_EXPENSE_DATE
                + ", COUNT(" + InfoTable.COL_EXPENSE_ID + ") AS '" + InfoTable.COL_AMOUNT + "'"
                + " FROM " + InfoTable.TABLE_EXPENSES
                + " WHERE " + InfoTable.COL_EXPENSE_DATE + " LIKE '" + date + "%'"
                + " GROUP BY " + InfoTable.COL_EXPENSE_DATE
                + " ORDER BY " + InfoTable.COL_EXPENSE_DATE;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String sDate = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXPENSE_DATE));
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

    public List<ObjectDate> getExpensetDateList() {
        db = myDatabase.getWritableDatabase();
        List<ObjectDate> objectDateList = new ArrayList<>();
        String getData = "SELECT " + InfoTable.COL_EXPENSE_DATE
                + ", COUNT(" + InfoTable.COL_EXPENSE_ID + ") AS '" + InfoTable.COL_AMOUNT + "'"
                + " FROM " + InfoTable.TABLE_EXPENSES
                + " GROUP BY " + InfoTable.COL_EXPENSE_DATE;

        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXPENSE_DATE));
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


}
