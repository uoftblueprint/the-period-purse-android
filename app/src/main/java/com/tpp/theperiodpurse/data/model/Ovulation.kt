package com.tpp.theperiodpurse.data.model

enum class Ovulation(val displayName: String) {
    Ovulating("Ovulating"),
    Not_Ovulating("Not Ovulating")
    ;

    companion object {
        fun getOvulationByDisplayName(displayName: String): Ovulation? {
            return Ovulation.values().find { it.displayName == displayName }
        }
    }
}
