package com.example.practical7

object PersonDbTableData {

    const val TABLE_PERSONS = "persons"
    const val KEY_ID = "id"
    const val KEY_NAME = "name"
    const val KEY_EMAIL = "email"
    const val KEY_PHONE = "phone"
    const val KEY_ADDRESS = "address"

    val CREATE_TABLE = ("CREATE TABLE $TABLE_PERSONS("
            + "$KEY_ID TEXT PRIMARY KEY,"
            + "$KEY_NAME TEXT,"
            + "$KEY_EMAIL TEXT,"
            + "$KEY_PHONE TEXT,"
            + "$KEY_ADDRESS TEXT)"
            )
}
