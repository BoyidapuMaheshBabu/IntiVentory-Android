package com.boyidapu.homestock.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.MeasurementDimension
import com.boyidapu.homestock.domain.model.Product
import com.boyidapu.homestock.domain.model.UnitType

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val categoryId: String,
    val name: String,
    val dimension: String,
    val preferredUnit: String,
    val isPreset: Boolean,
    val isArchived: Boolean,
    val hasEverHadTransactions: Boolean = false,
    val createdAtEpochMs: Long
) {
    fun toDomain(): Product {
        val parsedDimension = try {
            MeasurementDimension.valueOf(dimension)
        } catch (_: Exception) {
            MeasurementDimension.MASS
        }
        val parsedUnit = UnitType.fromSymbol(preferredUnit)
        val safePreferredUnit = if (parsedUnit.dimension == parsedDimension) {
            parsedUnit
        } else {
            parsedDimension.defaultUnit
        }

        return Product(
            id = id,
            category = FixedCategory.fromId(categoryId),
            name = name,
            dimension = parsedDimension,
            preferredUnit = safePreferredUnit,
            isPreset = isPreset,
            isArchived = isArchived,
            hasEverHadTransactions = hasEverHadTransactions,
            createdAtEpochMs = createdAtEpochMs
        )
    }

    companion object {
        fun fromDomain(product: Product): ProductEntity {
            val safeUnit = if (product.preferredUnit.dimension == product.dimension) {
                product.preferredUnit
            } else {
                product.dimension.defaultUnit
            }
            return ProductEntity(
                id = product.id,
                categoryId = product.category.id,
                name = product.name,
                dimension = product.dimension.name,
                preferredUnit = safeUnit.symbol,
                isPreset = product.isPreset,
                isArchived = product.isArchived,
                hasEverHadTransactions = product.hasEverHadTransactions,
                createdAtEpochMs = product.createdAtEpochMs
            )
        }
    }
}
