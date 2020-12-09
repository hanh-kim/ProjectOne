package vn.poly.personalmanagement.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.database.table.InfoTable;
import vn.poly.personalmanagement.model.User;

public class AccountDAO {

    MyDatabase myDatabase;
    SQLiteDatabase db;

    public AccountDAO(MyDatabase myDatabase) {
        this.myDatabase = myDatabase;
    }

    public long deleteData(String username) {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_ACCOUNT, InfoTable.COL_USERNAME + " = ?", new String[]{username});
    }

    public long updateData(User user) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_USERNAME, user.getUsername());
        values.put(InfoTable.COL_EMAIL, user.getEmail());
        values.put(InfoTable.COL_PASSWORD, user.getPassword());
        return db.update(InfoTable.TABLE_ACCOUNT, values, InfoTable.COL_EMAIL + " = ?", new String[]{user.getEmail()});
    }

    public long changePassword(String email, String password) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_PASSWORD, password);
        return db.update(InfoTable.TABLE_ACCOUNT, values, InfoTable.COL_EMAIL + " = ?", new String[]{email});
    }

    public long addData(User user) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_EMAIL, user.getEmail());
        values.put(InfoTable.COL_PASSWORD, user.getPassword());
        return db.insert(InfoTable.TABLE_ACCOUNT, null, values);
    }

    public List<User> getAllData() {
        db = myDatabase.getWritableDatabase();
        List<User> userList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_ACCOUNT + " ORDER BY " + InfoTable.COL_USERNAME + " ASC";
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_ID));
                String username = cursor.getString(cursor.getColumnIndex(InfoTable.COL_USERNAME));
                String email =cursor.getString(cursor.getColumnIndex(InfoTable.COL_EMAIL));
                String password = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PASSWORD));
                User user = new User(id,username,email,password);
                userList.add(user);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return userList;
    }

    public User getUser(String username) {
        db = myDatabase.getWritableDatabase();
        String getData = "SELECT * FROM " + InfoTable.TABLE_ACCOUNT + " WHERE " + InfoTable.COL_USERNAME + " = '" + username + "'";
        Cursor cursor = db.rawQuery(getData, null);
        User user = new User();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_ID));
            String sUsername = cursor.getString(cursor.getColumnIndex(InfoTable.COL_USERNAME));
            String sEmail =cursor.getString(cursor.getColumnIndex(InfoTable.COL_EMAIL));
            String sPassword = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PASSWORD));
            user = new User(id,sUsername,sEmail,sPassword);
            cursor.close();
            return user;
        }

        return user;
    }

    public User getUserWithEmail(String email) {
        db = myDatabase.getWritableDatabase();
        String getData = "SELECT * FROM " + InfoTable.TABLE_ACCOUNT + " WHERE " + InfoTable.COL_EMAIL + " = '" + email + "'";
        Cursor cursor = db.rawQuery(getData, null);
        User user = new User();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_ID));
            String sUsername = cursor.getString(cursor.getColumnIndex(InfoTable.COL_USERNAME));
            String sEmail =cursor.getString(cursor.getColumnIndex(InfoTable.COL_EMAIL));
            String sPassword = cursor.getString(cursor.getColumnIndex(InfoTable.COL_PASSWORD));
            user = new User(id,sUsername,sEmail,sPassword);
            cursor.close();
            return user;
        }

        return user;
    }


    // check exist
    public boolean checkExist(User user) {
        db = myDatabase.getWritableDatabase();
        String check = "SELECT * FROM " + InfoTable.TABLE_ACCOUNT + " WHERE " + InfoTable.COL_USERNAME + "= '" + user.getUsername() + "'";
        Cursor cursor = db.rawQuery(check, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean checkExist(String email, String password) {
        db = myDatabase.getWritableDatabase();
        String check = "SELECT * FROM " + InfoTable.TABLE_ACCOUNT + " WHERE " + InfoTable.COL_EMAIL + "= '" + email + "' AND " +
                InfoTable.COL_PASSWORD + " = '" + password + "'";
        Cursor cursor = db.rawQuery(check, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean checkExist(String email) {
        db = myDatabase.getWritableDatabase();
        String check = "SELECT * FROM " + InfoTable.TABLE_ACCOUNT + " WHERE " + InfoTable.COL_EMAIL + "= '" + email + "'";

        Cursor cursor = db.rawQuery(check, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }


}
