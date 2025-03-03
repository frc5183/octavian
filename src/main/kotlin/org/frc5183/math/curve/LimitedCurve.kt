package org.frc5183.math.curve

/**
 * Represents a curve that limits the output to a range of [[min], [max]]
 */
class LimitedCurve(
    val min: Double,
    val max: Double,
) : Curve {
    override operator fun invoke(input: Double): Double {
        return input.coerceIn(min, max)
    }
}