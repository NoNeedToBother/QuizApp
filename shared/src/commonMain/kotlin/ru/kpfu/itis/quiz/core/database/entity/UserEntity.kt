package ru.kpfu.itis.quiz.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val password: String,
    @ColumnInfo(name = "profile_picture_uri")
    val profilePictureUri: String = "",
    val info: String = "",
    @ColumnInfo(name = "date_registered")
    val dateRegistered: Int,
    @ColumnInfo(name = "signed_in")
    val isSignedIn: Boolean = false,
)
