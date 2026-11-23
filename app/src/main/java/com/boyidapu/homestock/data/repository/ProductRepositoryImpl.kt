package com.boyidapu.homestock.data.repository

import androidx.room.withTransaction
import com.boyidapu.homestock.data.local.AppDatabase
import com.boyidapu.homestock.data.local.dao.ProductDao
import com.boyidapu.homestock.data.local.dao.TransactionDao
import com.boyidapu.homestock.data.local.entity.ProductEntity
import com.boyidapu.homestock.data.local.seed.CatalogSeeder
import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.Product
import com.boyidapu.homestock.domain.model.ProductWithStock
import com.boyidapu.homestock.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val database: AppDatabase,
    private val productDao: ProductDao,
    private val transactionDao: TransactionDao
) : ProductRepository {

    override fun getProductsByCategory(category: FixedCategory): Flow<List<Product>> {
        return productDao.getProductsByCategory(category.id).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getProductsWithStockByCategory(category: FixedCategory): Flow<List<ProductWithStock>> {
        return productDao.getProductsWithStockByCategory(category.id).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getProductById(productId: Long): Flow<Product?> {
        return productDao.getProductById(productId).map { it?.toDomain() }
    }

    override fun getArchivedProducts(): Flow<List<Product>> {
        return productDao.getArchivedProducts().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getArchivedProductsWithStock(): Flow<List<ProductWithStock>> {
        return productDao.getArchivedProductsWithStock().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun insertProduct(product: Product): Long {
        return database.withTransaction {
            if (productDao.doesProductExistWithName(product.name.trim())) {
                throw IllegalArgumentException("A product with name '${product.name.trim()}' already exists")
            }
            productDao.insertProduct(ProductEntity.fromDomain(product))
        }
    }

    override suspend fun updateProduct(product: Product) {
        database.withTransaction {
            val existing = productDao.getProductByIdSync(product.id)
            if (existing != null) {
                if (existing.hasEverHadTransactions && (existing.dimension != product.dimension.name)) {
                    throw IllegalArgumentException("Cannot change measurement dimension of a product that has transaction history")
                }
                if (productDao.doesProductExistWithName(product.name.trim(), excludeProductId = product.id)) {
                    throw IllegalArgumentException("A product with name '${product.name.trim()}' already exists")
                }
                val updatedHasEverHad = existing.hasEverHadTransactions || product.hasEverHadTransactions
                val updatedProduct = product.copy(hasEverHadTransactions = updatedHasEverHad)
                productDao.updateProduct(ProductEntity.fromDomain(updatedProduct))
            } else {
                productDao.updateProduct(ProductEntity.fromDomain(product))
            }
        }
    }

    override suspend fun archiveProduct(productId: Long): Boolean {
        productDao.archiveProduct(productId)
        return true
    }

    override suspend fun restoreProduct(productId: Long) {
        productDao.restoreProduct(productId)
    }

    override suspend fun deleteProductAtomically(productId: Long): Boolean {
        database.withTransaction {
            productDao.deleteTransactionsForProduct(productId)
            productDao.deleteProductById(productId)
        }
        return true
    }

    override suspend fun getTransactionCountForProduct(productId: Long): Int {
        return productDao.getTransactionCountForProduct(productId)
    }

    override suspend fun doesProductExistWithName(name: String, excludeProductId: Long?): Boolean {
        return productDao.doesProductExistWithName(name.trim(), excludeProductId)
    }

    override suspend fun ensureCatalogSeeded() {
        CatalogSeeder.seedIfNeeded(database)
    }
}
