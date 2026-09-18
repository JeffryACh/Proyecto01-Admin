package com.example.proyecto01_administracion.ui.vehicle

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto01_administracion.domain.models.LegalDocument
import com.example.proyecto01_administracion.domain.repositories.DocumentRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

data class DocumentUiState(
    val documents: List<LegalDocument> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val type: String = "",
    val expirationDate: Long? = null,
    val fileUri: Uri? = null,
    val validationErrors: Map<String, String> = emptyMap()
)

@HiltViewModel
class DocumentViewModel @Inject constructor(
    private val documentRepository: DocumentRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DocumentUiState())
    val uiState: StateFlow<DocumentUiState> = _uiState.asStateFlow()

    fun loadDocuments(vehicleId: String) {
        if (vehicleId.isBlank()) {
            _uiState.update { it.copy(documents = emptyList(), isLoading = false, error = null) }
            return
        }
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            documentRepository.getDocumentsByVehicle(vehicleId)
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message ?: "No se pudieron cargar los documentos") } }
                .collect { docs -> _uiState.update { it.copy(documents = docs, isLoading = false, error = null) } }
        }
    }

    fun onTypeChange(v: String) = _uiState.update { it.copy(type = v, validationErrors = it.validationErrors - "type") }
    fun onExpirationDateChange(v: Long?) = _uiState.update { it.copy(expirationDate = v, validationErrors = it.validationErrors - "expirationDate") }
    fun onFileUriChange(v: Uri?) = _uiState.update { it.copy(fileUri = v, validationErrors = it.validationErrors - "file") }

    fun saveDocument(vehicleId: String) {
        val s = _uiState.value
        val errors = mutableMapOf<String, String>()

        if (s.type.trim().isBlank()) errors["type"] = "El tipo es obligatorio"
        if (s.fileUri == null) errors["file"] = "Debes seleccionar un archivo"
        
        // Expiration date might be optional depending on document type, 
        // but for now we'll just check if it's provided it's valid if required.
        // Let's assume it's mandatory for simplicity unless specified otherwise.
        if (s.expirationDate == null) errors["expirationDate"] = "La fecha de vencimiento es obligatoria"

        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(validationErrors = errors) }
            return
        }

        val doc = LegalDocument(
            id = UUID.randomUUID().toString(),
            vehiculo_id = vehicleId,
            tipo = s.type.trim(),
            fecha_vencimiento = Timestamp(Date(s.expirationDate!!)),
            archivo_url = s.fileUri.toString()
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
            documentRepository.saveDocument(doc).fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, isSuccess = true) } },
                onFailure = { e -> _uiState.update { it.copy(isLoading = false, error = e.message ?: "No se pudo guardar el documento") } }
            )
        }
    }

    fun resetForm() = _uiState.update { it.copy(type = "", expirationDate = null, fileUri = null, validationErrors = emptyMap(), isSuccess = false) }
}
