package com.example.forexinsight.domain.model

enum class DataType { REFERENCE_RATE, OHLC_CANDLE, BID_ASK, OTHER }

data class DatasetInfo(
    val id: Long = 0,
    val fileName: String,
    val dataType: DataType,
    val pair: String = "EUR/USD",
    val timeframe: String? = null,
    val source: String,
    val startDate: String?,
    val endDate: String?,
    val importedAt: Long,
    val validCount: Int,
    val rejectedCount: Int,
    val duplicateCount: Int,
    val validationStatus: String
)

data class ReferenceRate(
    val id: Long = 0,
    val datasetId: Long,
    val date: String,
    val rate: Double
)

data class ValidationReport(
    val valid: Int,
    val rejected: Int,
    val duplicates: Int,
    val errors: List<String>
)

data class RateStats(
    val first: Double?,
    val last: Double?,
    val min: Double?,
    val max: Double?,
    val mean: Double?,
    val median: Double?,
    val count: Int,
    val startDate: String?,
    val endDate: String?,
    val changeAbs: Double?,
    val changePct: Double?
)

data class ReturnPoint(val date: String, val simpleReturn: Double?, val logReturn: Double?)
