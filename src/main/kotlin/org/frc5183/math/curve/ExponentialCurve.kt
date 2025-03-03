package org.frc5183.math.curve

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sign

/**
 * Represents an exponential [Curve] with some modifications:
 * - Symmetric about the y-axis, allowing for negative inputs.
 * - Uses a base of 1 + [base].
 * - Divides by [base].
 *
 * The equation for this curve is y = ((x/abs(x)) * ((1 + [base])^abs(x) -1)) / [base]
 */
class ExponentialCurve(
    /**
     * The [base] constant in the equation.
     */
    val base: Double,
) : Curve {
    override operator fun invoke(input: Double): Double {
        val sign = if (input == 0.0) 1.0 else input.sign

        return sign * ((((1 + base).pow(abs(input)) - 1)) / base)
    }
}
