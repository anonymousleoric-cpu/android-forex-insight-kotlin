package com.example.forexinsight.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "datasets")
data class DatasetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fileName: String,
    val dataType: String,
    val pair: String,
    val timeframe: String?,
    val source: String,
    val startDate: String?,
    val endDate: String?,
    val importedAt: Long,
    val validCount: Int,
    val rejectedCount: Int,
    val duplicateCount: Int,
    val validationStatus: String
)

@Entity(tableName = "reference_rates", indices = [androidx.room.Index(value = ["datasetId", "date"], unique = true)])
data class ReferenceRateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val datasetId: Long,
    val date: String,
    val rate: Double
)
