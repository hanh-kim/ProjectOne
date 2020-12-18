package vn.poly.personalmanagement.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.database.table.InfoTable;
import vn.poly.personalmanagement.model.Exercise;

public class ExerciseDAO {

    private MyDatabase myDatabase;
    private SQLiteDatabase db;

    public ExerciseDAO(MyDatabase myDatabase) {
        this.myDatabase = myDatabase;
    }

    public long clearAllData() {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_EXERCISES, null, null);
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

    public long saveDataFromFirebase(List<Exercise> exerciseList) {

        for (Exercise exercise : exerciseList) {
            db = myDatabase.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(InfoTable.COL_EXERCISE_NAME, exercise.getExerciseName());
            long query = db.insert(InfoTable.TABLE_EXERCISES, null, values);
            if (query > 0) {
                return query;
            } else return 0;
        }
        return 0;
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

    public List<Exercise> getAllData() {
        db = myDatabase.getWritableDatabase();
        List<Exercise> exerciseList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_EXERCISES;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_EXERCISE_ID));
                String name = cursor.getString(cursor.getColumnIndex(InfoTable.COL_EXERCISE_NAME));
                Exercise exercise = new Exercise(id, name);
                exerciseList.add(exercise);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return exerciseList;
    }

}
