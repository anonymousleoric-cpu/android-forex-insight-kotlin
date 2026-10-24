package com.example.forexinsight.ui.screens.importcsv

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forexinsight.data.repository.ForexRepository
import com.example.forexinsight.domain.model.ValidationReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ImportState(
    val loading: Boolean = false,
    val report: ValidationReport? = null,
    val error: String? = null
)

class ImportViewModel(private val repo: ForexRepository) : ViewModel() {
    private val _state = MutableStateFlow(ImportState())
    val state: StateFlow<ImportState> = _state.asStateFlow()

    fun import(uri: Uri, fileName: String) {
        viewModelScope.launch {
            _state.value = ImportState(loading = true)
            try {
                val report = repo.importCsv(uri, fileName)
                _state.value = ImportState(loading = false, report = report)
            } catch (e: Exception) {
                _state.value = ImportState(loading = false, error = e.message ?: "خطا")
            }
        }
    }

    fun clear() {
        viewModelScope.launch { repo.clearAll() }
    }
}

@Composable
fun rememberCsvPicker(onPicked: (Uri, String) -> Unit): () -> Unit {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            onPicked(uri, uri.lastPathSegment ?: "file.csv")
        }
    }
    return remember { { launcher.launch(arrayOf("text/*", "text/csv", "*/*")) } }
}
