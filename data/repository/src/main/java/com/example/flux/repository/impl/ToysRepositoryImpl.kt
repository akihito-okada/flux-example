package com.example.flux.repository.impl

import com.example.flux.model.PurchasableWorksSortType
import com.example.flux.model.StoreSearchConditions
import com.example.flux.model.Toy
import com.example.flux.model.ToyCategory
import com.example.flux.model.ToyColor
import com.example.flux.model.ToyId
import com.example.flux.model.ToyMaterial
import com.example.flux.model.ToySaveRequest
import com.example.flux.model.ToysCount
import com.example.flux.model.util.CoroutinePlugin
import com.example.flux.preferences.UserPreferences
import com.example.flux.remote.ToysFactoryApiService
import com.example.flux.remote.request.toys.SaveToyRequest
import com.example.flux.remote.request.toys.SearchToysCountRequest
import com.example.flux.remote.request.toys.SearchToysRequest
import com.example.flux.repository.ToysRepository
import kotlinx.coroutines.withContext

class ToysRepositoryImpl(
    private val toysFactoryApiService: ToysFactoryApiService,
    private val userPreferences: UserPreferences,
) : ToysRepository {

    override suspend fun searchToys(conditions: StoreSearchConditions): List<Toy> {
        return withContext(CoroutinePlugin.ioDispatcher) {
            toysFactoryApiService.searchToys(
                SearchToysRequest(
                    ids = null,
                    materialIds = conditions.materialsTechniquesParameter,
                    colorIds = conditions.colorCategoriesParameter,
                    priceMax = conditions.maxPrice.apiRequestPriceMax,
                    priceMin = conditions.minPrice.apiRequestPriceMin,
                    createdYears = conditions.productionYearsTypesParameter,
                    keyword = null,
                    categoryIds = conditions.categoryIdsParameter,
                    userId = userPreferences.userId.value,
                    orderBy = conditions.selectedSortType.value,
                ),
            ).map { it.convert() }
        }
    }

    override suspend fun toyDetails(toyId: ToyId): Toy {
        val searchConditions = StoreSearchConditions(selectedToyIds = listOf(toyId))
        return withContext(CoroutinePlugin.ioDispatcher) {
            toysFactoryApiService.searchToys(
                SearchToysRequest(
                    ids = searchConditions.toyIdsParameter,
                    materialIds = null,
                    colorIds = null,
                    priceMax = null,
                    priceMin = null,
                    createdYears = null,
                    keyword = null,
                    categoryIds = null,
                    userId = userPreferences.userId.value,
                    orderBy = PurchasableWorksSortType.default.value,
                ),
            ).first().convert()
        }
    }

    override suspend fun searchToysCount(conditions: StoreSearchConditions): ToysCount {
        return withContext(CoroutinePlugin.ioDispatcher) {
            toysFactoryApiService.searchToysCount(
                SearchToysCountRequest(
                    ids = null,
                    materialIds = conditions.materialsTechniquesParameter,
                    colorIds = conditions.colorCategoriesParameter,
                    priceMax = conditions.maxPrice.apiRequestPriceMax,
                    priceMin = conditions.minPrice.apiRequestPriceMin,
                    createdYears = conditions.productionYearsTypesParameter,
                    keyword = null,
                    categoryIds = conditions.categoryIdsParameter,
                ),
            ).convert()
        }
    }

    override suspend fun saveToy(saveToyRequest: ToySaveRequest) {
        return withContext(CoroutinePlugin.ioDispatcher) {
            toysFactoryApiService.saveToy(
                SaveToyRequest(
                    userId = userPreferences.userId.value,
                    toyId = saveToyRequest.toy.id.value,
                ),
            )
        }
    }

    override suspend fun removeSavedToy(removeSavedToyRequest: ToySaveRequest) {
        return withContext(CoroutinePlugin.ioDispatcher) {
            val userId = userPreferences.userId.value
            val toyId = removeSavedToyRequest.toy.id.value
            toysFactoryApiService.removeSavedToy(
                userId = "eq.$userId",
                toyId = "eq.$toyId",
            )
        }
    }

    override suspend fun getCategories(): List<ToyCategory> {
        return withContext(CoroutinePlugin.ioDispatcher) {
            toysFactoryApiService.getCategories().map { it.convert() }
        }
    }

    override suspend fun getColors(): List<ToyColor> {
        return withContext(CoroutinePlugin.ioDispatcher) {
            toysFactoryApiService.getColors().map { it.convert() }
        }
    }

    override suspend fun getMaterials(): List<ToyMaterial> {
        return withContext(CoroutinePlugin.ioDispatcher) {
            toysFactoryApiService.getMaterials().map { it.convert() }
        }
    }
}
