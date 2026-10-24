package com.example.forexinsight.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forexinsight.data.db.entities.DatasetEntity
import com.example.forexinsight.data.db.entities.ReferenceRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DatasetDao {
    @Insert
    suspend fun insert(dataset: DatasetEntity): Long

    @Query("SELECT * FROM datasets ORDER BY importedAt DESC")
    fun observeAll(): Flow<List<DatasetEntity>>

    @Query("DELETE FROM datasets")
    suspend fun deleteAll()

    @Query("DELETE FROM datasets WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface ReferenceRateDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(rates: List<ReferenceRateEntity>)

    @Query("SELECT * FROM reference_rates WHERE datasetId = :datasetId ORDER BY date ASC")
    suspend fun getByDataset(datasetId: Long): List<ReferenceRateEntity>

    @Query("SELECT * FROM reference_rates WHERE datasetId = :datasetId ORDER BY date ASC")
    fun observeByDataset(datasetId: Long): Flow<List<ReferenceRateEntity>>

    @Query("DELETE FROM reference_rates WHERE datasetId = :datasetId")
    suspend fun deleteByDataset(datasetId: Long)
}
