package org.frc5183.math.curve

/**
 * Represents a linear [Curve] in the form of y = [m]x + [b]. (where y is the output and x is the input)
 */
class LinearCurve(
    /**
     * The slope of the line.
     */
    val m: Double,
    /**
     * The y-intercept of the line.
     */
    val b: Double,
) : Curve {
    override fun invoke(input: Double): Double = m * input + b
}
