package com.theperiodpurse.app.ui.state

data class LogUiState(
    var selectSquares: LinkedHashMap<Int, Any>,
    var promptToText: LinkedHashMap<Int, String>,
)
