package com.amex.fruitstand.order

import com.amex.fruitstand.discount.DiscountResult
import com.amex.fruitstand.discount.DiscountService
import com.amex.fruitstand.price.PriceService
import com.google.common.truth.Truth.assertThat
import org.junit.Ignore
import org.junit.Test
import java.math.BigInteger


/** This class could be written much more concisely using JUnit5's "parameterized test" (since
 * most of the test methods have the same assertions, and differ only in input) , but unfortunately
 *Bazel's test runner does not support JUnit5 by default out of the box.
 **/
class OrderServiceImplTest {

    /**OrderService only knows about the PriceService interface, the PriceService implementation
     *  is intentionally not on the classpath of this test, to guarantee that this test always remains
     *  independent of any implementation changes in PriceService. Because of this, we need to mock
     *  or create an implementation of PriceService.
     */

    private val priceService = object : PriceService {
        override fun getAppleUnitPrice(): Int {
            return 60;
        }

        override fun getOrangeUnitPrice(): Int {
            return 25;
        }
    }

    /** a 'discount service' implementation that gives 0% off, to preserve existing test assertions
     * that existed before the discount requirement was added to the problem statement.
     *  **/
    private val noDiscountService = object : DiscountService {
        override fun applyDiscount(orangeQty: Int, appleQty: Int): DiscountResult {
            return DiscountResult(effectiveOrangeQty = orangeQty, effectiveAppleQty = appleQty)
        }
    }
    private val orderService = OrderServiceImpl(priceService, noDiscountService)

    private val expectedResultIfInputInvalid = OrderSubmissionResult(orderPrice = null,
            errorMessage = OrderServiceImpl.INVALID_ORDER_ERROR_MSG)


    @Test
    fun emptyOrderString() {
        assertThat(expectedResultIfInputInvalid).isEqualTo(orderService.processOrder(""));
    }

    @Test
    fun multipleColons() {
        assertThat(expectedResultIfInputInvalid).isEqualTo(orderService.processOrder("3:2:1"));
    }

    @Test
    fun noColonsButNumerical() {
        assertThat(expectedResultIfInputInvalid).isEqualTo(orderService.processOrder("4"));
    }

    @Test
    fun validOrangeNonNumericalApple() {
        assertThat(expectedResultIfInputInvalid).isEqualTo(orderService.processOrder("3:abc"));
    }

    @Test
    fun validOrangeNegativeApple() {
        assertThat(expectedResultIfInputInvalid).isEqualTo(orderService.processOrder("3:-1"));
    }

    @Test
    fun validAppleNonNumericalOrange() {
        assertThat(expectedResultIfInputInvalid).isEqualTo(orderService.processOrder("abc:3"));
    }

    @Test
    fun validAppleNegativeOrange() {
        assertThat(expectedResultIfInputInvalid).isEqualTo(orderService.processOrder("-1:3"));
    }

    @Test
    fun nonNumericalOrderString() {
        assertThat(expectedResultIfInputInvalid).isEqualTo(orderService.processOrder("abc"));
    }

    @Test
    fun onePositiveQuantityNoColon() {
        assertThat(expectedResultIfInputInvalid).isEqualTo(orderService.processOrder("100"));
    }

    @Test
    fun oneNegativeQuantityNoColon() {
        assertThat(expectedResultIfInputInvalid).isEqualTo(orderService.processOrder("-100"))
    }

    @Test
    fun validOrangeValidApple() {
        val orangeQty = 3;
        val appleQty = 2;
        val expectedPrice = (orangeQty * priceService.getOrangeUnitPrice()).toBigInteger().plus(
                (appleQty * priceService.getAppleUnitPrice()).toBigInteger())

        assertThat(OrderSubmissionResult(orderPrice = expectedPrice, null))
                .isEqualTo(orderService.processOrder("${orangeQty}:${appleQty}"))
    }

    @Test
    fun zeroOrangeValidApple() {
        val orangeQty = 0;
        val appleQty = 2;
        val expectedPrice = (orangeQty * priceService.getOrangeUnitPrice()).toBigInteger().plus(
                (appleQty * priceService.getAppleUnitPrice()).toBigInteger())

        assertThat(OrderSubmissionResult(orderPrice = expectedPrice, null)).isEqualTo(
                orderService.processOrder("${orangeQty}:${appleQty}"))
    }

    @Test
    fun validOrangeZeroApple() {
        val orangeQty = 2;
        val appleQty = 0;
        val expectedPrice = (orangeQty * priceService.getOrangeUnitPrice()).toBigInteger().plus(
                (appleQty * priceService.getAppleUnitPrice()).toBigInteger())

        assertThat(OrderSubmissionResult(orderPrice = expectedPrice, null)).isEqualTo(
                orderService.processOrder("${orangeQty}:${appleQty}"))
    }

    /*
     *  explore behavior at the "extremes" of "valid" arguments.
     *  If we order the maximum # of apples AND the maximum # of oranges
     *  each at the maximum price, the answer should be 2*MAX_INT*MAX_INT and should not overflow
     * the orderPrice field of the OrderSubmissionResult. This test case indicates the service should
     * be refactored to restrict the max order size and max price to more reasonable values.
     */
    @Test
    @Ignore("interesting (ignored) test case exceeds the limit of BigInteger on 64 bit architectures, might be " +
            "a relevant test case someday in the future with technological advances lol")
    fun longOverflowIsHandled() {
        val orangeQty = Integer.MAX_VALUE
        val appleQty = Integer.MAX_VALUE
        val maxPriceService = object : PriceService {
            override fun getAppleUnitPrice() = Integer.MAX_VALUE
            override fun getOrangeUnitPrice() = Integer.MAX_VALUE
        }
        val expensiveOrderService = OrderServiceImpl(maxPriceService, noDiscountService);
        assertThat(
                OrderSubmissionResult(BigInteger.valueOf(2).times((
                        BigInteger.valueOf(Integer.MAX_VALUE.toLong())
                                .times(BigInteger.valueOf(Integer.MAX_VALUE.toLong())))),
                        errorMessage = null)).isEqualTo(expensiveOrderService.processOrder("${orangeQty}:${appleQty}"))
    }

}