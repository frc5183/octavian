package org.frc5183.math.curve

/**
 * Represents a quadratic [Curve] in the form of y = [a]x^2 + [b]x + [c]. (where y is the output and x is the input)
 */
class QuadraticCurve(
    /**
     * The coefficient of the x^2 term in the quadratic equation.
     */
    val a: Double,
    /**
     * The coefficient of the x term in the quadratic equation.
     */
    val b: Double,
    /**
     * The constant term in the quadratic equation.
     */
    val c: Double,
) : Curve {
    override fun invoke(input: Double): Double = a * input * input + b * input + c
}
