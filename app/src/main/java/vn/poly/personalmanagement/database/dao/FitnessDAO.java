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
import vn.poly.personalmanagement.model.Eating;
import vn.poly.personalmanagement.model.Fitness;
import vn.poly.personalmanagement.model.Meal;

public class FitnessDAO {

    private Mydatabase myDatabase;
    private SQLiteDatabase db;

    public FitnessDAO(Mydatabase myDatabase) {
        this.myDatabase = myDatabase;
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
        values.put(InfoTable.COL_DETAIL_EXERCISE_DATE, exercise.getDate());
        values.put(InfoTable.COL_DETAIL_EXERCISE_NAME, exercise.getExercise());
        values.put(InfoTable.COL_DETAIL_EXERCISE_DESCRIPTION, exercise.getDate());
        long query = db.update(InfoTable.TABLE_MEALS, values, InfoTable.COL_MEAL_ID + " = ?",
                new String[]{String.valueOf(exercise.getId())});
        if (query > 0) {
            return query;
        } else return 0;

    }

    public long updateFitnessDate(String oldDate, String newDate) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_MEAL_DATE, newDate);
        long query = db.update(InfoTable.TABLE_MEALS, values, InfoTable.COL_MEAL_DATE + " = ?",
                new String[]{oldDate});
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

    public List<Fitness> getFitnessList() {
        db = myDatabase.getWritableDatabase();
        List<Fitness> fitnessList = new ArrayList<>();
        String getData = "SELECT " + InfoTable.COL_DETAIL_EXERCISE_DATE + ", COUNT(" + InfoTable.COL_DETAIL_EXERCISE_ID + ") AS '" + InfoTable.COL_DETAIL_EXERCISE_AMOUNT + "'"
                + " FROM " + InfoTable.TABLE_DETAIL_EXERCISE
                + " GROUP BY " + InfoTable.COL_DETAIL_EXERCISE_DATE;

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
