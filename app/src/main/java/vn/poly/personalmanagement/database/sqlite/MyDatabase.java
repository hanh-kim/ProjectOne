package vn.poly.personalmanagement.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import vn.poly.personalmanagement.database.table.InfoTable;

public class MyDatabase extends SQLiteOpenHelper {

    public MyDatabase(@Nullable Context context) {
        super(context, "smartdaily.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        InfoTable.createTablePlans(db);
        InfoTable.createTableNotes(db);
        InfoTable.createTableIncome(db);
        InfoTable.createTableExpenses(db);
        InfoTable.createTableMeal(db);
        InfoTable.createTableFitness(db);
        InfoTable.createTableExercises(db);
        InfoTable.createTableAccount(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + InfoTable.TABLE_PLANS);
        db.execSQL("drop table if exists " + InfoTable.TABLE_NOTES);
        db.execSQL("drop table if exists " + InfoTable.TABLE_IMCOMES);
        db.execSQL("drop table if exists " + InfoTable.TABLE_EXPENSES);
        db.execSQL("drop table if exists " + InfoTable.TABLE_MEALS);
        db.execSQL("drop table if exists " + InfoTable.TABLE_DETAIL_EXERCISE);
        db.execSQL("drop table if exists " + InfoTable.TABLE_EXERCISES);
        db.execSQL("drop table if exists " + InfoTable.TABLE_ACCOUNT);
        onCreate(db);
    }
}
