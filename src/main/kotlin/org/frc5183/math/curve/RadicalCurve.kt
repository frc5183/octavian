package org.frc5183.math.curve

import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sqrt

/**
 * Represents a radical [Curve] in the form of y = (x/abs(input))*([a]sqrt(abs(x)) + [b]). (where y is the output and x is the input)
 * This differs from a normal radical curve because it is symmetric about the y-axis to allow for negative inputs.
 */
class RadicalCurve(
    /**
     * The coefficient of the sqrt(x) term in the radical equation.
     */
    val a: Double,
    /**
     * The constant term in the radical equation.
     */
    val b: Double,
) : Curve {
    override fun invoke(input: Double): Double {
        val sign = if (input == 0.0) 1.0 else input.sign

        return sign * (a * sqrt(abs(input)) + b)
    }
}
