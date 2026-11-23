package com.boyidapu.homestock.core.money

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MoneyUtilsTest {

    @Test
    fun parseRupeesToPaise_validInputs() {
        assertEquals(50000L, MoneyUtils.parseRupeesToPaise("500"))
        assertEquals(50000L, MoneyUtils.parseRupeesToPaise("500.00"))
        assertEquals(5050L, MoneyUtils.parseRupeesToPaise("50.5"))
        assertEquals(505L, MoneyUtils.parseRupeesToPaise("5.05"))
        assertEquals(0L, MoneyUtils.parseRupeesToPaise("0"))
        assertEquals(0L, MoneyUtils.parseRupeesToPaise("0.00"))
    }

    @Test
    fun parseRupeesToPaise_invalidInputs() {
        assertNull(MoneyUtils.parseRupeesToPaise("-50"))
        assertNull(MoneyUtils.parseRupeesToPaise("500.123"))
        assertNull(MoneyUtils.parseRupeesToPaise("abc"))
        assertNull(MoneyUtils.parseRupeesToPaise(""))
    }

    @Test
    fun formatPaiseToFormattedAmount_correctRepresentation() {
        assertEquals("₹500", MoneyUtils.formatPaiseToFormattedAmount(50000L))
        assertEquals("₹5.05", MoneyUtils.formatPaiseToFormattedAmount(505L))
        assertEquals("₹0", MoneyUtils.formatPaiseToFormattedAmount(0L))
    }

    @Test
    fun calculateAverageSpendingPaise_nearestPaiseRounding() {
        // 101 paise across 3 purchases -> 33.666... -> 34 paise
        assertEquals(34L, MoneyUtils.calculateAverageSpendingPaise(101L, 3))

        // 100 paise across 3 purchases -> 33.333... -> 33 paise
        assertEquals(33L, MoneyUtils.calculateAverageSpendingPaise(100L, 3))

        // 101 paise across 2 purchases -> 50.5 -> 51 paise (HALF_UP)
        assertEquals(51L, MoneyUtils.calculateAverageSpendingPaise(101L, 2))

        // Exact division: 600 paise / 3 = 200 paise
        assertEquals(200L, MoneyUtils.calculateAverageSpendingPaise(600L, 3))

        // Free purchases: 0 paise / 3 = 0 paise
        assertEquals(0L, MoneyUtils.calculateAverageSpendingPaise(0L, 3))

        // Zero or negative purchase count returns null
        assertNull(MoneyUtils.calculateAverageSpendingPaise(100L, 0))
        assertNull(MoneyUtils.calculateAverageSpendingPaise(100L, -1))
    }
}
