package com.tugas.capstoneproject_historia.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
class HistoryEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @field:ColumnInfo(name = "title")
    val title: String,

    @field:ColumnInfo(name = "date")
    var date: String,

    @field:ColumnInfo(name = "imageUri")
    var imageUri: String?,

    @field:ColumnInfo(name = "confidenceScore")
    var confidenceScore: Int,
//
//    @field:ColumnInfo(name = "lat")
//    var lat: Float,
//
//    @field:ColumnInfo(name = "lon")
//    var lon: Float,
//
//    @field:ColumnInfo(name = "isScanned")
//    var isScanned: Boolean? = false,
)