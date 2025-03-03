package org.frc5183.math.curve

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PiecewiseCurveTest {
    @Test
    fun `test piecewise curve with multiple conditions`() {
        val curve =
            PiecewiseCurve(
                linkedMapOf(
                    // For x < 0, y = -x (negate the input)
                    { input: Double -> input < 0.0 } to Curve { input: Double -> -1.0 * input },
                    // For 0 <= x < 1, y = 2x (double the input)
                    { input: Double -> input >= 0.0 && input < 1.0 } to Curve { input: Double -> 2.0 * input },
                    // For x >= 1, y = x + 1
                    { input: Double -> input >= 1.0 } to Curve { input: Double -> input + 1.0 },
                ),
            )

        assertEquals(1.0, curve(-1.0), 0.0001) // -(-1.0) = 1.0
        assertEquals(0.0, curve(0.0), 0.0001) // 2.0 * 0.0 = 0.0
        assertEquals(1.0, curve(0.5), 0.0001) // 2.0 * 0.5 = 1.0
        assertEquals(2.0, curve(1.0), 0.0001) // 1.0 + 1.0 = 2.0
        assertEquals(3.0, curve(2.0), 0.0001) // 2.0 + 1.0 = 3.0
    }

    @Test
    fun `test piecewise curve with no matching conditions`() {
        val curve =
            PiecewiseCurve(
                linkedMapOf(
                    { input: Double -> input < -100.0 } to Curve { input: Double -> input },
                ),
            )

        assertEquals(0.0, curve(0.0), 0.0001)
        assertEquals(0.0, curve(1.0), 0.0001)
        assertEquals(0.0, curve(-1.0), 0.0001)
    }

    @Test
    fun `test piecewise curve with empty conditions`() {
        val curve = PiecewiseCurve(linkedMapOf())

        assertEquals(0.0, curve(0.0), 0.0001)
        assertEquals(0.0, curve(1.0), 0.0001)
        assertEquals(0.0, curve(-1.0), 0.0001)
    }
}
