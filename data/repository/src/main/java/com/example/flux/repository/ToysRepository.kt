package com.example.flux.repository

import com.example.flux.model.StoreSearchConditions
import com.example.flux.model.Toy
import com.example.flux.model.ToyCategory
import com.example.flux.model.ToyColor
import com.example.flux.model.ToyId
import com.example.flux.model.ToyMaterial
import com.example.flux.model.ToySaveRequest
import com.example.flux.model.ToysCount

interface ToysRepository {
    suspend fun searchToys(conditions: StoreSearchConditions): List<Toy>
    suspend fun toyDetails(toyId: ToyId): Toy
    suspend fun searchToysCount(conditions: StoreSearchConditions): ToysCount
    suspend fun saveToy(saveToyRequest: ToySaveRequest)
    suspend fun removeSavedToy(removeSavedToyRequest: ToySaveRequest)
    suspend fun getCategories(): List<ToyCategory>
    suspend fun getColors(): List<ToyColor>
    suspend fun getMaterials(): List<ToyMaterial>
}

