package com.definityfirst.jesusgonzalez.agendaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.UiThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jesus.gonzalez on 27/01/2017.
 */


public class DatabaseHandler extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 3;
    //Database Name
    private static final String DATABASE_NAME = "AppAgenda";
    //Table Name
    private static final String TABLE_NAME = "Contactos";
    //Column Name
    private static final String KEY_ID = "id";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_NUMERO = "numero";
    private static final String KEY_EMAIL = "email";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREAR_TABLA = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NOMBRE + " TEXT,"
                + KEY_NUMERO + " TEXT," + KEY_EMAIL + " TEXT" + ")";
        db.execSQL(CREAR_TABLA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addContact(Contacto contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE, contact.getName()); // Contact Name
        values.put(KEY_NUMERO, contact.getNumber()); // Contact Phone
        values.put(KEY_EMAIL, contact.getEmail()); // Contact Email

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    Contacto getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_NOMBRE, KEY_NUMERO, KEY_EMAIL }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contacto contact = new Contacto(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return contact
        return contact;
    }

    // Getting All Contacts
    public List<Contacto> getAllContacts() {
        List<Contacto> contactList = new ArrayList<Contacto>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY nombre";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contacto contact = new Contacto();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setNumber(cursor.getString(2));
                contact.setEmail(cursor.getString(3));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updateContact(Contacto contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE, contact.getName());
        values.put(KEY_NUMERO, contact.getNumber());
        values.put(KEY_EMAIL, contact.getEmail());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single contact
    public void deleteContact(Contacto contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);


        // return count
        return cursor.getCount();
    }
}
