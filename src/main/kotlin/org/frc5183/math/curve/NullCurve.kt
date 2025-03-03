package org.frc5183.math.curve

/**
 * Represents a curve which always returns 0.
 */
class NullCurve : Curve {
    override fun invoke(input: Double): Double = 0.0
}
