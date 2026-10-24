package com.example.forexinsight.domain.usecase

import com.example.forexinsight.domain.model.RateStats
import com.example.forexinsight.domain.model.ReferenceRate
import com.example.forexinsight.domain.model.ReturnPoint
import kotlin.math.ln

class StatsUseCase {
    fun compute(rates: List<ReferenceRate>): RateStats {
        if (rates.isEmpty()) return RateStats(null,null,null,null,null,null,0,null,null,null,null)
        val sorted = rates.sortedBy { it.date }
        val values = sorted.map { it.rate }
        val first = values.first()
        val last = values.last()
        val min = values.minOrNull()
        val max = values.maxOrNull()
        val mean = values.average()
        val median = median(values)
        val changeAbs = last - first
        val changePct = if (first != 0.0) (last / first - 1.0) * 100.0 else null
        return RateStats(first, last, min, max, mean, median, values.size,
            sorted.first().date, sorted.last().date, changeAbs, changePct)
    }

    fun returns(rates: List<ReferenceRate>): List<ReturnPoint> {
        val sorted = rates.sortedBy { it.date }
        val out = mutableListOf<ReturnPoint>()
        for (i in 1 until sorted.size) {
            val prev = sorted[i-1].rate
            val cur = sorted[i].rate
            val simple = if (prev != 0.0) (cur / prev - 1.0) else null
            val logR = if (prev > 0 && cur > 0) ln(cur / prev) else null
            out.add(ReturnPoint(sorted[i].date, simple, logR))
        }
        return out
    }

    fun sma(rates: List<ReferenceRate>, period: Int): List<Pair<String, Double>> {
        val sorted = rates.sortedBy { it.date }
        val out = mutableListOf<Pair<String, Double>>()
        for (i in sorted.indices) {
            if (i + 1 < period) continue
            val window = sorted.subList(i + 1 - period, i + 1).map { it.rate }
            out.add(sorted[i].date to window.average())
        }
        return out
    }

    private fun median(values: List<Double>): Double {
        val s = values.sorted()
        val n = s.size
        return if (n % 2 == 1) s[n/2] else (s[n/2 - 1] + s[n/2]) / 2.0
    }
}
