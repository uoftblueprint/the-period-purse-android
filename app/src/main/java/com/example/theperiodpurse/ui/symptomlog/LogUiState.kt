package com.example.theperiodpurse.ui.symptomlog

data class LogUiState (
    var selectSquares: LinkedHashMap<String, Any>,
    var promptToText: LinkedHashMap<String, String>
)