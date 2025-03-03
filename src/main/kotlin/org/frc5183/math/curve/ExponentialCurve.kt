package org.frc5183.math.curve

import kotlin.math.abs
import kotlin.math.pow

/**
 * Represents an exponential [Curve] with some modifications:
 * - Symmetric about the y-axis, allowing for negative inputs.
 * - Uses a base of 1 + [base].
 * - Returns 0 if the input is 0.
 * - Divides by [base].
 *
 * The equation for this curve is y = ((x/abs(x)) * ((1 + [base])^abs(x) -1)) / [base]
 */
class ExponentialCurve(
    /**
     * The [base] constant in the equation.
     */
    val base: Double
) : Curve {
    override operator fun invoke(input: Double): Double {
        if (input == 0.0) {
            return 0.0
        }

        return ((input / abs(input)) * ((1 + base).pow(abs(input)) - 1)) / base
    }
}