package com.example.forexinsight.data.csv

import android.content.Context
import android.net.Uri
import com.example.forexinsight.data.db.entities.DatasetEntity
import com.example.forexinsight.data.db.entities.ReferenceRateEntity
import com.example.forexinsight.domain.model.DataType
import com.example.forexinsight.domain.model.ValidationReport
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class CsvImportResult(
    val dataset: DatasetEntity,
    val rates: List<ReferenceRateEntity>,
    val report: ValidationReport
)

class CsvImporter(private val context: Context) {

    fun import(uri: Uri, fileName: String): CsvImportResult {
        val errors = mutableListOf<String>()
        val seenDates = mutableSetOf<String>()
        val rates = mutableListOf<ReferenceRateEntity>()
        var rejected = 0
        var duplicates = 0
        var sourceColumnPresent = true
        var sourceValue = "Unknown"

        context.contentResolver.openInputStream(uri)?.use { stream ->
            val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
            val headerLine = reader.readLine()
            if (headerLine == null) {
                errors.add("فایل خالی است")
                return buildResult(fileName, rates, 0, rejected, duplicates, errors, "Unknown", null, null)
            }
            val headers = headerLine.split(",").map { it.trim().lowercase() }
            val dateIdx = headers.indexOf("date")
            val rateIdx = headers.indexOf("eur_usd_reference_rate")
            val sourceIdx = headers.indexOf("source")
            if (dateIdx < 0 || rateIdx < 0) {
                errors.add("ستون‌های date یا eur_usd_reference_rate یافت نشد")
                return buildResult(fileName, rates, 0, rejected, duplicates, errors, "Unknown", null, null)
            }
            if (sourceIdx < 0) {
                sourceColumnPresent = false
                sourceValue = "Unknown"
            }

            val formatter = DateTimeFormatter.ISO_LOCAL_DATE
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val row = line!!.trim()
                if (row.isEmpty()) continue
                val cols = row.split(",")
                if (cols.size <= maxOf(dateIdx, rateIdx)) {
                    rejected++; errors.add("ردیف ناقص: $row"); continue
                }
                val dateStr = cols[dateIdx].trim()
                val rateStr = cols[rateIdx].trim()
                val src = if (sourceIdx >= 0 && sourceIdx < cols.size) cols[sourceIdx].trim() else "Unknown"
                if (src.isNotEmpty() && src != "Unknown") sourceValue = src

                // validate date
                val dateOk = try {
                    LocalDate.parse(dateStr, formatter); true
                } catch (e: DateTimeParseException) { false }
                if (!dateOk) { rejected++; errors.add("تاریخ نامعتبر: $dateStr"); continue }

                // validate rate
                val rate = rateStr.toDoubleOrNull()
                if (rate == null || !rate.isFinite() || rate <= 0) { rejected++; errors.add("نرخ نامعتبر: $rateStr"); continue }

                if (seenDates.contains(dateStr)) { duplicates++; continue }
                seenDates.add(dateStr)

                rates.add(ReferenceRateEntity(0, 0, dateStr, rate))
            }
        } ?: run { errors.add("امکان خواندن فایل وجود ندارد") }

        val sorted = rates.sortedBy { it.date }
        val startDate = sorted.firstOrNull()?.date
        val endDate = sorted.lastOrNull()?.date
        val source = if (sourceColumnPresent) sourceValue else "Unknown"
        val report = ValidationReport(sorted.size, rejected, duplicates, errors.take(50))
        val dataset = DatasetEntity(
            id = 0,
            fileName = fileName,
            dataType = DataType.REFERENCE_RATE.name,
            pair = "EUR/USD",
            timeframe = null,
            source = source,
            startDate = startDate,
            endDate = endDate,
            importedAt = System.currentTimeMillis(),
            validCount = sorted.size,
            rejectedCount = rejected,
            duplicateCount = duplicates,
            validationStatus = if (errors.isEmpty()) "OK" else "WARN"
        )
        val withDatasetId = sorted.map { it.copy(datasetId = 0) } // placeholder; set later in repo
        return CsvImportResult(dataset, withDatasetId, report)
    }

    private fun buildResult(
        fileName: String, rates: List<ReferenceRateEntity>, valid: Int, rejected: Int, duplicates: Int,
        errors: List<String>, source: String, start: String?, end: String?
    ): CsvImportResult {
        val dataset = DatasetEntity(
            0, fileName, DataType.REFERENCE_RATE.name, "EUR/USD", null, source, start, end,
            System.currentTimeMillis(), valid, rejected, duplicates,
            if (errors.isEmpty()) "OK" else "WARN"
        )
        return CsvImportResult(dataset, rates, ValidationReport(valid, rejected, duplicates, errors))
    }
}
