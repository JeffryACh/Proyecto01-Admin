package com.example.proyecto01_administracion.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 =
    object : Migration(1, 2) {

        override fun migrate(
            database: SupportSQLiteDatabase
        ) {
            database.execSQL(
                """
                ALTER TABLE users
                ADD COLUMN password TEXT
                NOT NULL DEFAULT 'default'
                """.trimIndent()
            )
        }
    }