package com.example.mad_24012011102_practical_7

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
        db.execSQL("DROP TABLE IF EXISTS " + PersonDbTableData.TABLE_NAME)
        onCreate(db)
    }

    private fun getValues(person: Person): ContentValues {
        val values = ContentValues()
        values.put(PersonDbTableData.COLUMN_ID, person.id)
        values.put(PersonDbTableData.COLUMN_PERSON_NAME, person.name)
        values.put(PersonDbTableData.COLUMN_PERSON_EMAIL_ID, person.emailId)
        values.put(PersonDbTableData.COLUMN_PERSON_PHONE_NO, person.phoneNo)
        values.put(PersonDbTableData.COLUMN_PERSON_ADDRESS, person.address)
        values.put(PersonDbTableData.COLUMN_PERSON_GPS_LAT, person.latitude)
        values.put(PersonDbTableData.COLUMN_PERSON_GPS_LONG, person.longitude)
        return values
    }

    fun insertPerson(person: Person): Long {
        val db = writableDatabase
        val id = db.insertWithOnConflict(
            PersonDbTableData.TABLE_NAME,
            null,
            getValues(person),
            SQLiteDatabase.CONFLICT_REPLACE
        )
        db.close()
        return id
    }

    fun updatePerson(person: Person): Int {
        val db = writableDatabase
        val rows = db.update(
            PersonDbTableData.TABLE_NAME,
            getValues(person),
            PersonDbTableData.COLUMN_ID + " = ?",
            arrayOf(person.id)
        )
        db.close()
        return rows
    }

    fun deletePerson(person: Person) {
        val db = writableDatabase
        db.delete(
            PersonDbTableData.TABLE_NAME,
            PersonDbTableData.COLUMN_ID + " = ?",
            arrayOf(person.id)
        )
        db.close()
    }

    fun getPerson(id: String): Person? {
        val db = readableDatabase
        val cursor = db.query(
            PersonDbTableData.TABLE_NAME, null,
            PersonDbTableData.COLUMN_ID + " = ?", arrayOf(id),
            null, null, null
        )
        val person = if (cursor.moveToFirst()) getPerson(cursor) else null
        cursor.close()
        db.close()
        return person
    }

    private fun getPerson(cursor: Cursor): Person {
        return Person(
            id = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_NAME)),
            emailId = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_EMAIL_ID)),
            phoneNo = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_PHONE_NO)),
            address = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_ADDRESS)),
            latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_GPS_LAT)),
            longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_GPS_LONG))
        )
    }

    val allPersons: ArrayList<Person>
        get() {
            val persons = ArrayList<Person>()
            val db = readableDatabase
            val cursor = db.query(PersonDbTableData.TABLE_NAME, null, null, null, null, null, null)
            if (cursor.moveToFirst()) {
                do {
                    persons.add(getPerson(cursor))
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return persons
        }

    val personsCount: Int
        get() {
            val db = readableDatabase
            val cursor = db.query(PersonDbTableData.TABLE_NAME, null, null, null, null, null, null)
            val count = cursor.count
            cursor.close()
            db.close()
            return count
        }
}
