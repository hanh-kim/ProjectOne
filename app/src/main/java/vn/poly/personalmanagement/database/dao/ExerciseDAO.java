package vn.poly.personalmanagement.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.poly.personalmanagement.database.sqlite.Mydatabase;
import vn.poly.personalmanagement.database.table.InfoTable;
import vn.poly.personalmanagement.model.Eating;
import vn.poly.personalmanagement.model.Exercise;
import vn.poly.personalmanagement.model.Meal;

public class ExerciseDAO {

    private Mydatabase myDatabase;
    private SQLiteDatabase db;

    public ExerciseDAO(Mydatabase myDatabase) {
        this.myDatabase = myDatabase;
    }

    public long deleteData(Exercise exercise) {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_EXERCISES, InfoTable.COL_EXERCISE_ID + " = ?",
                new String[]{String.valueOf(exercise.getId())});
    }


    public long addData(Exercise exercise) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_EXERCISE_NAME, exercise.getExerciseName());
        long query = db.insert(InfoTable.TABLE_EXERCISES, null, values);
        if (query > 0) {
            return query;
        } else return 0;
    }

    public long updateData(Exercise exercise) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_EXERCISE_NAME, exercise.getExerciseName());
        long query = db.update(InfoTable.TABLE_EXERCISES, values, InfoTable.COL_EXERCISE_ID + " = ?",
                new String[]{String.valueOf(exercise.getId())});
        if (query > 0) {
            return query;
        } else return 0;

    }
    public List<Exercise> getAll() {
        db = myDatabase.getWritableDatabase();
        List<Exercise> exerciseList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_EXERCISES ;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_EXERCISE_ID));
                String name = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXERCISE_NAME));
                Exercise exercise = new Exercise(id,name);
                exerciseList.add(exercise);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return exerciseList;
    }

//    public List<Meal> getResultSearchedWithDate(String date) {
//        db = myDatabase.getWritableDatabase();
//        List<Meal> mealList = new ArrayList<>();
//        String getData = "SELECT * FROM " + InfoTable.TABLE_MEALS + " WHERE " + InfoTable.COL_MEAL_DATE + " LIKE '" + date + "%' "
//                + "ORDER BY " + InfoTable.COL_MEAL_DATE;
//
//        Cursor cursor = db.rawQuery(getData, null);
//        if (cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                int meal_id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_MEAL_ID));
//                String meal_title = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_TITLE));
//                String meal_time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_TIME));
//                String meal_date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DATE));
//                String meal_detail = cursor.getString(cursor.getColumnIndex(InfoTable.COL_MEAL_DETAIL));
//                Meal meal = new Meal(meal_id, meal_title, meal_date,meal_time, meal_detail);
//                mealList.add(meal);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        }
//
//        return mealList;
//    }



}
