package com.example.forexinsight.data.repository

import android.content.Context
import android.net.Uri
import com.example.forexinsight.data.csv.CsvImportResult
import com.example.forexinsight.data.csv.CsvImporter
import com.example.forexinsight.data.db.AppDatabase
import com.example.forexinsight.data.db.entities.ReferenceRateEntity
import com.example.forexinsight.domain.model.DatasetInfo
import com.example.forexinsight.domain.model.ReferenceRate
import com.example.forexinsight.domain.model.ValidationReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ForexRepository(private val db: AppDatabase, private val context: Context) {

    private val datasetDao = db.datasetDao()
    private val rateDao = db.referenceRateDao()

    fun observeDatasets(): Flow<List<DatasetInfo>> =
        datasetDao.observeAll().map { list ->
            list.map { e ->
                DatasetInfo(
                    id = e.id, fileName = e.fileName,
                    dataType = com.example.forexinsight.domain.model.DataType.valueOf(e.dataType),
                    pair = e.pair, timeframe = e.timeframe, source = e.source,
                    startDate = e.startDate, endDate = e.endDate, importedAt = e.importedAt,
                    validCount = e.validCount, rejectedCount = e.rejectedCount,
                    duplicateCount = e.duplicateCount, validationStatus = e.validationStatus
                )
            }
        }

    suspend fun importCsv(uri: Uri, fileName: String): ValidationReport {
        val importer = CsvImporter(context)
        val result = importer.import(uri, fileName)
        val datasetId = datasetDao.insert(result.dataset)
        val ratesWithId = result.rates.map { it.copy(datasetId = datasetId) }
        rateDao.insertAll(ratesWithId)
        return result.report
    }

    suspend fun getRates(datasetId: Long): List<ReferenceRate> =
        rateDao.getByDataset(datasetId).map { ReferenceRate(it.id, it.datasetId, it.date, it.rate) }

    fun observeRates(datasetId: Long): Flow<List<ReferenceRate>> =
        rateDao.observeByDataset(datasetId).map { list -> list.map { ReferenceRate(it.id, it.datasetId, it.date, it.rate) } }

    suspend fun clearAll() {
        datasetDao.deleteAll()
    }
}
