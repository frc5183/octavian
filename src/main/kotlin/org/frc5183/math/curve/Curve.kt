package org.frc5183.math.curve

/**
 * Represents a mathematical [Curve] (function) that can return an output for a given input.
 *
 * This only handles curving inputs, it does no controller deadband filtering, it's simply
 * a lambda that takes a double and returns a double.
 * For deadband filtering, it's recommended to use a [PiecewiseCurve] where input < deadband returns 0.
 */
interface Curve : (Double) -> Double {
    companion object {
        /**
         * Creates a [Curve] from a lambda.
         */
        operator fun invoke(curve: (Double) -> Double): Curve =
            object : Curve {
                override operator fun invoke(input: Double): Double = curve(input)
            }
    }
}
