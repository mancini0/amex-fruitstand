package com.amex.fruitstand.discount

class DiscountServiceImpl(val orangeDiscounters: List<(Int) -> Int>,
                          val appleDiscounters: List<(Int) -> Int>) : DiscountService {

    companion object CommonDiscounts {
        /**a function that returns a function which calculates the effective quantity
         * associated with discounts of the type "x for the price of y". Provided here for convenience
         * but instantiators of this class are free to construct instances using whatever discount
         * functions they please.
         */
        val xForPriceOfY = fun(x: Int, y: Int): (Int) -> Int {
            return fun(qty: Int): Int {
                return ((qty / x) * y) + (qty % x)
            }
        }

    }

    /**Given a list of applicable discounts, apply each one, and use the one that is the most
     * effective, i.e, the one that results in the minimum effective quantity.
     */
    override fun applyDiscount(orangeQty: Int, appleQty: Int): DiscountResult {
        return DiscountResult(effectiveOrangeQty = orangeDiscounters.map { it(orangeQty) }.minOf { it },
                effectiveAppleQty = appleDiscounters.map { it(appleQty) }.minOf { it })
    }

}

