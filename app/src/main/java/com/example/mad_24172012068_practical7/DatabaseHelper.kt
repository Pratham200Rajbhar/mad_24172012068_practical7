package com.example.mad_24172012068_practical7

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.ArrayList

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "persons_db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(PersonDbTableData.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + PersonDbTableData.TABLE_PERSONS)
        onCreate(db)
    }

    fun insertPerson(person: Person): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(PersonDbTableData.KEY_ID, person.id)
        values.put(PersonDbTableData.KEY_NAME, person.name)
        values.put(PersonDbTableData.KEY_EMAIL, person.emailId)
        values.put(PersonDbTableData.KEY_PHONE, person.phoneNo)
        values.put(PersonDbTableData.KEY_ADDRESS, person.address)
        val id = db.insertWithOnConflict(PersonDbTableData.TABLE_PERSONS, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
        return id
    }

    private fun getPersonFromCursor(cursor: Cursor): Person {
        val person = Person()
        person.id = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.KEY_ID))
        person.name = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.KEY_NAME))
        person.emailId = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.KEY_EMAIL))
        person.phoneNo = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.KEY_PHONE))
        person.address = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.KEY_ADDRESS))
        return person
    }

    val allPersons: ArrayList<Person>
        get() {
            val list = ArrayList<Person>()
            val selectQuery = "SELECT * FROM " + PersonDbTableData.TABLE_PERSONS
            val db = readableDatabase
            val c = db.rawQuery(selectQuery, null)
            if (c.moveToFirst()) {
                do {
                    val p = getPersonFromCursor(c)
                    list.add(p)
                } while (c.moveToNext())
            }
            c.close()
            db.close()
            return list
        }

    fun deletePerson(person: Person): Int {
        val db = writableDatabase
        val rows = db.delete(PersonDbTableData.TABLE_PERSONS,
            "${PersonDbTableData.KEY_ID}=?",
            arrayOf(person.id))
        db.close()
        return rows
    }
}
