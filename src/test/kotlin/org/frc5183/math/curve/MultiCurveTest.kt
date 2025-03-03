package org.frc5183.math.curve

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.pow

class MultiCurveTest {
    private companion object {
        private const val DELTA = 0.0001
    }

    @Test
    fun `test multi curve with linear curves`() {
        val curve1 = LinearCurve(2.0, 0.0) // y = 2x
        val curve2 = LinearCurve(1.0, 1.0) // y = x + 1
        val multiCurve = MultiCurve(listOf(curve1, curve2))

        // Test various inputs
        // Input 0.0 -> curve1(0.0) = 0.0 -> curve2(0.0) = 1.0
        assertEquals(1.0, multiCurve(0.0), DELTA)

        // Input 1.0 -> curve1(1.0) = 2.0 -> curve2(2.0) = 3.0
        assertEquals(3.0, multiCurve(1.0), DELTA)

        // Input -1.0 -> curve1(-1.0) = -2.0 -> curve2(-2.0) = -1.0
        assertEquals(-1.0, multiCurve(-1.0), DELTA)
    }

    @Test
    fun `test multi curve with mixed curves`() {
        val curve1 = LinearCurve(2.0, 0.0) // y = 2x
        val curve2 = LimitedCurve(0.0, 2.0) // limit output to [0, 2]
        val multiCurve = MultiCurve(listOf(curve1, curve2))

        // Test various inputs
        // Input 0.0 -> curve1(0.0) = 0.0 -> curve2(0.0) = 0.0
        assertEquals(0.0, multiCurve(0.0), DELTA)

        // Input 1.0 -> curve1(1.0) = 2.0 -> curve2(2.0) = 2.0
        assertEquals(2.0, multiCurve(1.0), DELTA)

        // Input -1.0 -> curve1(-1.0) = -2.0 -> curve2(-2.0) = 0.0 (limited)
        assertEquals(0.0, multiCurve(-1.0), DELTA)

        // Input 2.0 -> curve1(2.0) = 4.0 -> curve2(4.0) = 2.0 (limited)
        assertEquals(2.0, multiCurve(2.0), DELTA)
    }

    @Test
    fun `test multi curve with empty list`() {
        val multiCurve = MultiCurve(listOf())

        // With no curves, the input should be returned as is
        assertEquals(0.0, multiCurve(0.0), DELTA)
        assertEquals(1.0, multiCurve(1.0), DELTA)
        assertEquals(-1.0, multiCurve(-1.0), DELTA)
    }

    @Test
    fun `test multi curve with complex chain`() {
        val curve1 = LinearCurve(2.0, 0.0)               // y = 2x
        val curve2 = ExponentialCurve(1.0)               // Exponential transformation
        val curve3 = LimitedCurve(-3.0, 3.0)             // Limit to [-3, 3]
        val multiCurve = MultiCurve(listOf(curve1, curve2, curve3))

        // Input 0.0 -> curve1(0.0) = 0.0 -> curve2(0.0) = 0.0 -> curve3(0.0) = 0.0
        assertEquals(0.0, multiCurve(0.0), DELTA)

        // Input 1.0 -> curve1(1.0) = 2.0 -> curve2(2.0) = 3.0 -> curve3(3.0) = 3.0
        assertEquals(3.0, multiCurve(1.0), DELTA)

        // Input 2.0 -> curve1(2.0) = 4.0 -> curve2(4.0) = 15.0 -> curve3(15.0) = 3.0 (limited)
        val expected = Math.min(3.0, ((1 + 1.0).pow(4.0) - 1) / 1.0)
        assertEquals(expected, multiCurve(2.0), DELTA)

        // Input -1.0 -> curve1(-1.0) = -2.0 -> curve2(-2.0) = -3.0 -> curve3(-3.0) = -3.0
        assertEquals(-3.0, multiCurve(-1.0), DELTA)
    }
}
