package com.amex.fruitstand.discount;

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DiscountServiceImplTest {

    /** create a discounter that allows 3 oranges for the price of 2, and 'buy one get one' apples**/

    val discountService: DiscountService =
            DiscountServiceImpl(orangeDiscounters = listOf(DiscountServiceImpl.xForPriceOfY(3, 2)),
                    appleDiscounters = listOf(DiscountServiceImpl.xForPriceOfY(2, 1)))


    @Test
    fun applyDiscountZeroOrangesZeroApples() {
        assertThat(discountService.applyDiscount(orangeQty = 0, appleQty = 0))
                .isEqualTo(DiscountResult(effectiveOrangeQty = 0, effectiveAppleQty = 0))
    }


    @Test
    fun applyDiscount3Oranges2Apples() {
        assertThat(discountService.applyDiscount(orangeQty = 3, appleQty = 2))
                .isEqualTo(DiscountResult(effectiveOrangeQty = 2, effectiveAppleQty = 1))
    }

    @Test
    fun applyDiscount2Oranges1Apples() {
        assertThat(discountService.applyDiscount(orangeQty = 2, appleQty = 1))
                .isEqualTo(DiscountResult(effectiveOrangeQty = 2, effectiveAppleQty = 1))
    }

    @Test
    fun applyDiscount3Oranges0Apples() {
        assertThat(discountService.applyDiscount(orangeQty = 3, appleQty = 0))
                .isEqualTo(DiscountResult(effectiveOrangeQty = 2, effectiveAppleQty = 0))
    }

    @Test
    fun applyDiscount9Oranges7Apples() {
        assertThat(discountService.applyDiscount(orangeQty = 9, appleQty = 7))
                .isEqualTo(DiscountResult(effectiveOrangeQty = 6, effectiveAppleQty = 4))
    }

    /**My discount service can handle the scenario where multiple discounts are available for
     * a given commodity. It applies the most powerful applicable discount.
     */
    @Test
    fun applyMostPowerfulDiscountWhenMultipleAvailable() {
        val discountServiceMultiple = DiscountServiceImpl(orangeDiscounters =
        listOf(DiscountServiceImpl.xForPriceOfY(4, 1), DiscountServiceImpl.xForPriceOfY(6, 2)),
                appleDiscounters = listOf(DiscountServiceImpl.xForPriceOfY(2, 1)))

        // 12 oranges @ 4 for 1 = 3 effective qty
        // 12 oranges @ 6 for 2 = 4 effective qty, so the service should apply the 4 for 1 discount.
        assertThat(discountServiceMultiple.applyDiscount(orangeQty = 12, appleQty = 2))
                .isEqualTo(DiscountResult(effectiveOrangeQty = 3, effectiveAppleQty = 1))
    }
}