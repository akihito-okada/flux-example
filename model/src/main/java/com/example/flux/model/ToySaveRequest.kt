package com.example.flux.model

data class ToySaveRequest(
    val toy: Toy = Toy(),
    val hasSaved: Boolean = false,
)
