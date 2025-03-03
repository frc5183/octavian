package org.frc5183.math.curve

/**
 * Represents a curve that is a combination of multiple [Curve]s.
 *
 * This takes the output from the first curve and feeds it into the second curve, and so on.
 * Input -> Curve1 -> Curve2 -> ... -> CurveN -> Output
 */
class MultiCurve(
    /**
     * The list of [Curve]s to combine. Applied in the order they are in the list.
     */
    val curves: List<Curve>,
) : Curve {
    override fun invoke(input: Double): Double {
        var currentInput = input
        for (curve in curves) {
            currentInput = curve(currentInput)
        }
        return currentInput
    }
}
