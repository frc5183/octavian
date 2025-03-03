package org.frc5183.math.curve

/**
 * Represents a mathematical piecewise function.
 *
 * An example usage of this is to have a deadband filter where input < deadband returns 0.
 *
 * Returns 0 if no [Curve] has a matching condition.
 */
class PiecewiseCurve(
    /**
     * A map of boolean-returning lambdas to [Curve]s.
     * The first lambda that returns true will be used to calculate the output.
     */
    val curves: LinkedHashMap<(Double) -> Boolean, Curve>,
) : Curve {
    override fun invoke(input: Double): Double {
        for ((condition, curve) in curves) {
            if (condition(input)) {
                return curve(input)
            }
        }

        return 0.0
    }
}
