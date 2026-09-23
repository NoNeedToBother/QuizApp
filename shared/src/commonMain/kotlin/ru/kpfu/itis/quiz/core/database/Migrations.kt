package ru.kpfu.itis.quiz.core.database

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
            CREATE TABLE users_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                username TEXT NOT NULL,
                password TEXT NOT NULL,
                profile_picture_uri TEXT NOT NULL,
                info TEXT NOT NULL,
                date_registered INTEGER NOT NULL,
                signed_in INTEGER NOT NULL
            )
            """.trimIndent()
        )

        val selectStmt = connection.prepare(
            "SELECT id, username, password, profile_picture_uri, info, date_registered, signed_in FROM users"
        )
        val insertStmt = connection.prepare(
            """
            INSERT INTO users_new
                (id, username, password, profile_picture_uri, info, date_registered, signed_in)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """.trimIndent()
        )

        try {
            while (selectStmt.step()) {
                val id = selectStmt.getLong(0)
                val username = selectStmt.getText(1)
                val password = selectStmt.getText(2)
                val profilePictureUri = selectStmt.getText(3)
                val info = selectStmt.getText(4)
                val rawDate = selectStmt.getText(5)
                val isSignedIn = selectStmt.getLong(6)

                insertStmt.bindLong(1, id)
                insertStmt.bindText(2, username)
                insertStmt.bindText(3, password)
                insertStmt.bindText(4, profilePictureUri)
                insertStmt.bindText(5, info)
                insertStmt.bindInt(6, parseToEpochDays(rawDate))
                insertStmt.bindLong(7, isSignedIn)
                insertStmt.step()
                insertStmt.reset()
            }
        } finally {
            selectStmt.close()
            insertStmt.close()
        }

        connection.execSQL("DROP TABLE users")
        connection.execSQL("ALTER TABLE users_new RENAME TO users")
    }
}

private fun parseToEpochDays(raw: String): Int {
    val parts = raw.split('/')
    val day = parts[0].toInt()
    val month = Month.valueOf(parts[1])
    val year = parts[2].toInt()
    return LocalDate(year = year, month = month, dayOfMonth = day).toEpochDays()
}