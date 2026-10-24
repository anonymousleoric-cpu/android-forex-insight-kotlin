package com.example.forexinsight

import com.example.forexinsight.domain.model.ReferenceRate
import com.example.forexinsight.domain.usecase.StatsUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StatsUseCaseTest {
    @Test
    fun computesBasicStats() {
        val rates = listOf(
            ReferenceRate(1, 1, "2024-01-01", 1.1),
            ReferenceRate(2, 1, "2024-01-02", 1.2)
        )
        val s = StatsUseCase().compute(rates)
        assertEquals(2, s.count)
        assertEquals(1.1, s.min!!, 1e-9)
        assertEquals(1.2, s.max!!, 1e-9)
        assertTrue(s.changeAbs!! > 0)
    }

    @Test
    fun emptyReturnsNull() {
        val s = StatsUseCase().compute(emptyList())
        assertEquals(0, s.count)
        assertEquals(null, s.min)
    }
}
