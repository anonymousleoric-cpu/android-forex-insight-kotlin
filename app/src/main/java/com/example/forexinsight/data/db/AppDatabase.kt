package com.example.forexinsight.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.forexinsight.data.db.dao.DatasetDao
import com.example.forexinsight.data.db.dao.ReferenceRateDao
import com.example.forexinsight.data.db.entities.DatasetEntity
import com.example.forexinsight.data.db.entities.ReferenceRateEntity

@Database(
    entities = [DatasetEntity::class, ReferenceRateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun datasetDao(): DatasetDao
    abstract fun referenceRateDao(): ReferenceRateDao
}
