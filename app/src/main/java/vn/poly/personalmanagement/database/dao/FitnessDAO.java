package vn.poly.personalmanagement.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.database.table.InfoTable;
import vn.poly.personalmanagement.model.DetailExercise;
import vn.poly.personalmanagement.model.Fitness;

public class FitnessDAO {

    private MyDatabase myDatabase;
    private SQLiteDatabase db;

    public FitnessDAO(MyDatabase myDatabase) {
        this.myDatabase = myDatabase;
    }

    public long clearAllData() {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_DETAIL_EXERCISE, null, null);
    }

    public long deleteData(DetailExercise exercise) {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_DETAIL_EXERCISE, InfoTable.COL_DETAIL_EXERCISE_ID + " = ?", new String[]{String.valueOf(exercise.getId())});
    }

    public long deleteDataWithDate(String date) {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_DETAIL_EXERCISE, InfoTable.COL_DETAIL_EXERCISE_DATE + " = ?", new String[]{date});
    }

    public long addData(DetailExercise exercise) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_DETAIL_EXERCISE_DATE, exercise.getDate());
        values.put(InfoTable.COL_DETAIL_EXERCISE_NAME, exercise.getExercise());
        values.put(InfoTable.COL_DETAIL_EXERCISE_DESCRIPTION, exercise.getDescribe());
        long query = db.insert(InfoTable.TABLE_DETAIL_EXERCISE, null, values);
        if (query > 0) {
            return query;
        } else return 0;
    }

    public long updateData(DetailExercise exercise) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_DETAIL_EXERCISE_NAME, exercise.getExercise());
        values.put(InfoTable.COL_DETAIL_EXERCISE_DATE, exercise.getDate());
        values.put(InfoTable.COL_DETAIL_EXERCISE_DESCRIPTION, exercise.getDescribe());
        long query = db.update(InfoTable.TABLE_DETAIL_EXERCISE, values, InfoTable.COL_DETAIL_EXERCISE_ID + " = ?",
                new String[]{String.valueOf(exercise.getId())});
        if (query > 0) {
            return query;
        } else return 0;

    }

    public List<DetailExercise> getAllExerciseWithDate(String date) {
        db = myDatabase.getWritableDatabase();
        List<DetailExercise> detailExerciseList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_DETAIL_EXERCISE + "" +
                " WHERE " + InfoTable.COL_DETAIL_EXERCISE_DATE + " ='" + date + "'";
        //     + " ORDER BY " + InfoTable.COL_DETAIL_EXERCISE_DATE;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int exercise_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_DETAIL_EXERCISE_ID));
                String exercise_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_DETAIL_EXERCISE_DATE));
                String exercise_name = cursor.getString(cursor.getColumnIndex(InfoTable.COL_DETAIL_EXERCISE_NAME));
                String exercise_detail = cursor.getString(cursor.getColumnIndex(InfoTable.COL_DETAIL_EXERCISE_DESCRIPTION));

                DetailExercise exercise = new DetailExercise(exercise_id, exercise_date, exercise_name, exercise_detail);
                detailExerciseList.add(exercise);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return detailExerciseList;
    }

    public List<Fitness> getFitnessList() {
        db = myDatabase.getWritableDatabase();
        List<Fitness> fitnessList = new ArrayList<>();
        String getData = "SELECT " + InfoTable.COL_DETAIL_EXERCISE_DATE + ", COUNT(" + InfoTable.COL_DETAIL_EXERCISE_ID + ") AS '" + InfoTable.COL_DETAIL_EXERCISE_AMOUNT + "'"
                + " FROM " + InfoTable.TABLE_DETAIL_EXERCISE
                + " GROUP BY " + InfoTable.COL_DETAIL_EXERCISE_DATE
                + " ORDER BY " + InfoTable.COL_DETAIL_EXERCISE_DATE;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_DETAIL_EXERCISE_DATE));
                int amount = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_DETAIL_EXERCISE_AMOUNT));
                Fitness fitness = new Fitness();
                fitness.setDate(date);
                fitness.setAmountExercises(amount);
                fitnessList.add(fitness);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Collections.reverse(fitnessList);
        return fitnessList;
    }

    public List<Fitness> getFitnessList(String sDate) {

        db = myDatabase.getWritableDatabase();
        List<Fitness> fitnessList = new ArrayList<>();
        String getData = "SELECT " + InfoTable.COL_DETAIL_EXERCISE_DATE + ", COUNT(" + InfoTable.COL_DETAIL_EXERCISE_ID + ") AS '" + InfoTable.COL_DETAIL_EXERCISE_AMOUNT + "'"
                + " FROM " + InfoTable.TABLE_DETAIL_EXERCISE
                + " WHERE " + InfoTable.COL_DETAIL_EXERCISE_DATE + " LIKE '" + sDate + "%'"
                + " GROUP BY " + InfoTable.COL_DETAIL_EXERCISE_DATE
                + " ORDER BY " + InfoTable.COL_DETAIL_EXERCISE_DATE;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_DETAIL_EXERCISE_DATE));
                int amount = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_DETAIL_EXERCISE_AMOUNT));
                Fitness fitness = new Fitness();
                fitness.setDate(date);
                fitness.setAmountExercises(amount);
                fitnessList.add(fitness);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Collections.reverse(fitnessList);
        return fitnessList;
    }

    public long updateFitnessDate(String oldDate, String newDate) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_MEAL_DATE, newDate);
        long query = db.update(InfoTable.TABLE_DETAIL_EXERCISE, values, InfoTable.COL_DETAIL_EXERCISE_DATE + " = ?",
                new String[]{oldDate});
        if (query > 0) {
            return query;
        } else return 0;

    }

    public List<DetailExercise> getResultSearchedWithDate(String date) {
        db = myDatabase.getWritableDatabase();
        List<DetailExercise> detailExerciseList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_DETAIL_EXERCISE + " WHERE " + InfoTable.COL_DETAIL_EXERCISE_DATE + " LIKE '" + date + "%' "
                + "ORDER BY " + InfoTable.COL_DETAIL_EXERCISE_DATE;

        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int exercise_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_MEAL_ID));
                String exercise_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_DETAIL_EXERCISE_DATE));
                String exercise_name = cursor.getString(cursor.getColumnIndex(InfoTable.COL_DETAIL_EXERCISE_NAME));
                String exercise_detail = cursor.getString(cursor.getColumnIndex(InfoTable.COL_DETAIL_EXERCISE_DESCRIPTION));

                DetailExercise exercise = new DetailExercise(exercise_id, exercise_date, exercise_name, exercise_detail);
                detailExerciseList.add(exercise);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return detailExerciseList;
    }


    public List<DetailExercise> getAllData() {
        db = myDatabase.getWritableDatabase();
        List<DetailExercise> detailExerciseList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_DETAIL_EXERCISE;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int exercise_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_DETAIL_EXERCISE_ID));
                String exercise_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_DETAIL_EXERCISE_DATE));
                String exercise_name = cursor.getString(cursor.getColumnIndex(InfoTable.COL_DETAIL_EXERCISE_NAME));
                String exercise_detail = cursor.getString(cursor.getColumnIndex(InfoTable.COL_DETAIL_EXERCISE_DESCRIPTION));

                DetailExercise exercise = new DetailExercise(exercise_id, exercise_date, exercise_name, exercise_detail);
                detailExerciseList.add(exercise);
                cursor.moveToNext();
            }
            cursor.close();
        }
//        Collections.reverse(mealList);
        return detailExerciseList;
    }
}
