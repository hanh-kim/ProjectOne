package vn.poly.personalmanagement.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.poly.personalmanagement.database.sqlite.MyDatabase;
import vn.poly.personalmanagement.database.table.InfoTable;
import vn.poly.personalmanagement.model.Note;

public class NotesDAO {
    private MyDatabase myDatabase;
    private SQLiteDatabase db;

    public NotesDAO(MyDatabase myDatabase) {
        this.myDatabase = myDatabase;
    }

    public long deleteData(Note note) {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_NOTES, InfoTable.COL_NOTE_ID + " = ?", new String[]{String.valueOf(note.getId())});
    }
    public long clearData() {
        db = myDatabase.getWritableDatabase();
        return db.delete(InfoTable.TABLE_NOTES, InfoTable.COL_NOTE_IS_DELETED + " = ?", new String[]{String.valueOf(1)});
    }
    public long addData(Note note) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_NOTE_TITLE, note.getTitle());
        values.put(InfoTable.COL_NOTE_FOLDER_ID, note.getFolderID());
        values.put(InfoTable.COL_NOTE_DATE, note.getDate());
        values.put(InfoTable.COL_NOTE_TIME, note.getTime());
        values.put(InfoTable.COL_NOTE_CONTENT, note.getContent());
        values.put(InfoTable.COL_NOTE_IS_DELETED, note.getIsDeleted());
        long query = db.insert(InfoTable.TABLE_NOTES, null, values);
        if (query > 0) {
            return query;
        } else return 0;
    }

    public long updateData(Note note) {
        db = myDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoTable.COL_NOTE_TITLE, note.getTitle());
        values.put(InfoTable.COL_NOTE_FOLDER_ID, note.getFolderID());
        values.put(InfoTable.COL_NOTE_DATE, note.getDate());
        values.put(InfoTable.COL_NOTE_TIME, note.getTime());
        values.put(InfoTable.COL_NOTE_CONTENT, note.getContent());
        values.put(InfoTable.COL_NOTE_IS_DELETED, note.getIsDeleted());
        long query = db.update(InfoTable.TABLE_NOTES, values, InfoTable.COL_NOTE_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        if (query > 0) {
            return query;
        } else return 0;

    }

    public List<Note> getAllData(int fragId) {
        db = myDatabase.getWritableDatabase();
        List<Note> noteList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_NOTES + " WHERE "+ InfoTable.COL_NOTE_IS_DELETED+"=0 " +
                " AND "+InfoTable.COL_NOTE_FOLDER_ID+"="+fragId+"" ;
              //  +"  ORDER BY " + InfoTable.COL_NOTE_DATE + " DESC";
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                int noteId = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_NOTE_ID));
                String noteTitle = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_TITLE));
                int folderId = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_NOTE_FOLDER_ID));
                String date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_DATE));
                String time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_TIME));
                String content = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_CONTENT));
                int isDeleted = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_NOTE_IS_DELETED));
                Note note = new Note(noteId, noteTitle, folderId, date, time, content, isDeleted);
                noteList.add(note);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Collections.reverse(noteList);
        return noteList;
    }

    public List<Note> getAllDataDeleted() {
        db = myDatabase.getWritableDatabase();
        List<Note> noteList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_NOTES + " WHERE "+ InfoTable.COL_NOTE_IS_DELETED+"=1" ;
           //   +  " ORDER BY " + InfoTable.COL_NOTE_DATE + " DESC ";
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                int noteId = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_NOTE_ID));
                String noteTitle = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_TITLE));
                int folderId = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_NOTE_FOLDER_ID));
                String date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_DATE));
                String time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_TIME));
                String content = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_CONTENT));
                int isDeleted = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_NOTE_IS_DELETED));
                Note note = new Note(noteId, noteTitle, folderId, date, time, content, isDeleted);
                noteList.add(note);
                cursor.moveToNext();
            }
            cursor.close();

        }
        Collections.reverse(noteList);
        return noteList;
    }

    public Note getNote(int noteID) {
        db = myDatabase.getWritableDatabase();
        String getData = "SELECT * FROM " + InfoTable.TABLE_NOTES + " WHERE " + InfoTable.COL_NOTE_ID + " = " + noteID + "";
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int noteId = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_NOTE_ID));
            String noteTitle = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_TITLE));
            int folderId = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_NOTE_FOLDER_ID));
            String date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_DATE));
            String time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_TIME));
            String content = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_CONTENT));
            int isDeleted = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_NOTE_IS_DELETED));
            Note note = new Note(noteId, noteTitle, folderId, date, time, content, isDeleted);
            cursor.close();
            return note;
        }
        return null;
    }

    public List<Note> getResultSearched(String text) {
        db = myDatabase.getWritableDatabase();
        List<Note> noteList = new ArrayList<>();
        String getData = "SELECT * FROM " + InfoTable.TABLE_NOTES + " WHERE " + InfoTable.COL_NOTE_TITLE + " LIKE '" + text + "%' " +
                "AND "+InfoTable.COL_NOTE_IS_DELETED +"=0 ORDER BY " + InfoTable.COL_NOTE_FOLDER_ID + " ASC ";
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int noteId = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_NOTE_ID));
                String noteTitle = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_TITLE));
                int folderId = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_NOTE_FOLDER_ID));
                String date = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_DATE));
                String time = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_TIME));
                String content = cursor.getString(cursor.getColumnIndex(InfoTable.COL_NOTE_CONTENT));
                int isDeleted = cursor.getInt(cursor.getColumnIndex(InfoTable.COL_NOTE_IS_DELETED));
                Note note = new Note(noteId, noteTitle, folderId, date, time, content, isDeleted);
                noteList.add(note);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return noteList;
    }

    public int countNotes(int fragId){
        int count=0;
        db = myDatabase.getWritableDatabase();
        String getData = "SELECT * FROM " + InfoTable.TABLE_NOTES + " WHERE "+ InfoTable.COL_NOTE_IS_DELETED+"=0 " +
                " AND "+InfoTable.COL_NOTE_FOLDER_ID+"="+fragId;
        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                count++;
                cursor.moveToNext();
            }
            cursor.close();
        }
       return count;
    }

    public int countNoteDeleted(){
        int count=0;
        db = myDatabase.getWritableDatabase();
        String getData = "SELECT * FROM " + InfoTable.TABLE_NOTES + " WHERE "+ InfoTable.COL_NOTE_IS_DELETED+"=1 " ;

        Cursor cursor = db.rawQuery(getData, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                count++;
                cursor.moveToNext();
            }
            cursor.close();
        }
        return count;
    }


//    // check exist
//    public boolean checkExist(InfoUser user) {
//        db = myDatabase.getWritableDatabase();
//        String check = "SELECT * FROM " + InfoTable.TABLE_ACCOUNT + " WHERE " + InfoTable.COL_USERNAME + "= '" + user.username + "'";
//        Cursor cursor = db.rawQuery(check, null);
//
//        if (cursor.getCount() > 0) {
//            db.close();
//            return true;
//        } else {
//            db.close();
//            return false;
//        }
//    }
//
//    public boolean checkExist(String username, String password) {
//        db = myDatabase.getWritableDatabase();
//        String check = "SELECT * FROM " + InfoTable.TABLE_ACCOUNT + " WHERE " + InfoTable.COL_USERNAME + "= '" + username + "' AND " +
//                InfoTable.COL_PASSWORD + " = '" + password + "'";
//        Cursor cursor = db.rawQuery(check, null);
//
//        if (cursor.getCount() > 0) {
//            db.close();
//            return true;
//        } else {
//            db.close();
//            return false;
//        }
//    }

}
