package com.boyidapu.homestock.data.local.seed

import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.MeasurementDimension
import com.boyidapu.homestock.domain.model.UnitType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogSeederTest {

    @Test
    fun starterProducts_contains15ProductsAcrossThreeCategories() {
        val products = CatalogSeeder.STARTER_PRODUCTS
        assertEquals(15, products.size)

        val foodProducts = products.filter { it.categoryId == FixedCategory.FOOD_KITCHEN.id }
        val homeCareProducts = products.filter { it.categoryId == FixedCategory.HOME_CARE_CLEANING.id }
        val personalCareProducts = products.filter { it.categoryId == FixedCategory.PERSONAL_CARE_COSMETICS.id }

        assertEquals(5, foodProducts.size)
        assertEquals(5, homeCareProducts.size)
        assertEquals(5, personalCareProducts.size)
    }

    @Test
    fun starterProducts_allHavePresetTrueAndArchivedFalse() {
        CatalogSeeder.STARTER_PRODUCTS.forEach { product ->
            assertTrue(product.isPreset)
            assertFalse(product.isArchived)
        }
    }

    @Test
    fun starterProducts_haveValidDimensionAndUnitPairs() {
        val rice = CatalogSeeder.STARTER_PRODUCTS.first { it.name == "Rice" }
        assertEquals(MeasurementDimension.MASS.name, rice.dimension)
        assertEquals(UnitType.KG.symbol, rice.preferredUnit)

        val oil = CatalogSeeder.STARTER_PRODUCTS.first { it.name == "Oil" }
        assertEquals(MeasurementDimension.VOLUME.name, oil.dimension)
        assertEquals(UnitType.L.symbol, oil.preferredUnit)

        val soap = CatalogSeeder.STARTER_PRODUCTS.first { it.name == "Soap" }
        assertEquals(MeasurementDimension.COUNT.name, soap.dimension)
        assertEquals(UnitType.PIECES.symbol, soap.preferredUnit)

        val shampoo = CatalogSeeder.STARTER_PRODUCTS.first { it.name == "Shampoo" }
        assertEquals(MeasurementDimension.VOLUME.name, shampoo.dimension)
        assertEquals(UnitType.ML.symbol, shampoo.preferredUnit)
    }

    @Test
    fun toDomain_preservesPersistedPreferredUnit() {
        val spicesEntity = CatalogSeeder.STARTER_PRODUCTS.first { it.name == "Spices" }
        val spicesDomain = spicesEntity.toDomain()
        assertEquals(UnitType.G, spicesDomain.preferredUnit)

        val shampooEntity = CatalogSeeder.STARTER_PRODUCTS.first { it.name == "Shampoo" }
        val shampooDomain = shampooEntity.toDomain()
        assertEquals(UnitType.ML, shampooDomain.preferredUnit)

        val hairOilEntity = CatalogSeeder.STARTER_PRODUCTS.first { it.name == "Hair Oil" }
        val hairOilDomain = hairOilEntity.toDomain()
        assertEquals(UnitType.ML, hairOilDomain.preferredUnit)
    }

    @Test
    fun roundTrip_domainAndEntityPreservePreferredUnit() {
        CatalogSeeder.STARTER_PRODUCTS.forEach { entity ->
            val domain = entity.toDomain()
            val convertedBack = com.boyidapu.homestock.data.local.entity.ProductEntity.fromDomain(domain)
            assertEquals(entity.preferredUnit, convertedBack.preferredUnit)
            assertEquals(domain.preferredUnit, convertedBack.toDomain().preferredUnit)
        }
    }

    @Test
    fun toDomain_incompatibleUnitFallsBackToDimensionDefault() {
        val invalidEntity = com.boyidapu.homestock.data.local.entity.ProductEntity(
            id = 999L,
            categoryId = FixedCategory.FOOD_KITCHEN.id,
            name = "Broken Product",
            dimension = MeasurementDimension.MASS.name,
            preferredUnit = UnitType.L.symbol, // Invalid: Liquid unit for Mass dimension
            isPreset = false,
            isArchived = false,
            createdAtEpochMs = 1000L
        )

        val domain = invalidEntity.toDomain()
        assertEquals(UnitType.KG, domain.preferredUnit) // Falls back to Mass default (KG)
    }
}
