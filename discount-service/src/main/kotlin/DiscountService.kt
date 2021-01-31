package com.amex.fruitstand.discount

data class DiscountResult(val effectiveOrangeQty: Int, val effectiveAppleQty: Int)

interface DiscountService {

    /**Given an actual purchased quantity of oranges and apples, return the number of oranges and
     * apples that were not given away for free (the 'effective quantity'), after applying discounts.
     */
    fun applyDiscount(orangeQty: Int, appleQty: Int): DiscountResult
}