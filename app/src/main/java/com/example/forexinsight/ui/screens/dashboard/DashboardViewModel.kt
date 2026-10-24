package com.example.forexinsight.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forexinsight.data.repository.ForexRepository
import com.example.forexinsight.domain.model.DatasetInfo
import com.example.forexinsight.domain.model.RateStats
import com.example.forexinsight.domain.usecase.StatsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class DashboardState(
    val dataset: DatasetInfo? = null,
    val stats: RateStats? = null,
    val loading: Boolean = false
)

class DashboardViewModel(
    private val repo: ForexRepository,
    private val statsUseCase: StatsUseCase = StatsUseCase()
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val datasets = repo.observeDatasets().first()
            val ds = datasets.firstOrNull()
            val rates = if (ds != null) repo.getRates(ds.id) else emptyList()
            val stats = statsUseCase.compute(rates)
            _state.value = DashboardState(dataset = ds, stats = stats, loading = false)
        }
    }
}
