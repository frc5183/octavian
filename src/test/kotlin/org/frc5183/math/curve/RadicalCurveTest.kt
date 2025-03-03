package org.frc5183.math.curve

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.math.sqrt

class RadicalCurveTest {
    private companion object {
        private const val DELTA = 0.0001
    }

    @Test
    fun `test radical curve with positive a and b`() {
        val a = 1.0
        val b = 0.0
        val curve = RadicalCurve(a, b)

        assertEquals(0.0, curve(0.0), DELTA)

        assertEquals(sqrt(1.0), curve(1.0), DELTA) // 1.0/1.0 * (1.0 * sqrt(1.0) + 0.0) = 1.0
        assertEquals(2.0 / 2.0 * (1.0 * sqrt(2.0) + 0.0), curve(2.0), DELTA)

        assertEquals(-sqrt(1.0), curve(-1.0), DELTA) // -1.0/1.0 * (1.0 * sqrt(1.0) + 0.0) = -1.0
        assertEquals(-2.0 / 2.0 * (1.0 * sqrt(2.0) + 0.0), curve(-2.0), DELTA)
    }

    @Test
    fun `test radical curve with positive a and negative b`() {
        val a = 1.0
        val b = -1.0
        val curve = RadicalCurve(a, b)

        assertEquals(0.0, curve(1.0), DELTA) // 1.0 * sqrt(1.0) - 1.0 = 0.0
        assertEquals(1.0 * sqrt(abs(4.0)) - 1.0, curve(4.0), DELTA) //  1.0 * sqrt(abs(4.0)) - 1.0 = 1.0

        assertEquals(0.0, curve(-1.0), DELTA) // -1.0 * (1.0 * sqrt(1.0) - 1.0) = 0.0
        assertEquals(-1.0 * (1.0 * sqrt(abs(-4.0)) - 1.0), curve(-4.0), DELTA) // -1.0 * (1.0 * sqrt(abs(4.0)) - 1.0) = -1.0
    }

    @Test
    fun `test radical curve with negative a and positive b`() {
        val a = -1.0
        val b = 1.0
        val curve = RadicalCurve(a, b)

        assertEquals(0.0, curve(1.0), DELTA) // -1.0 * sqrt(1.0) - 1.0 = 0.0
        assertEquals((-1.0 * sqrt(abs(4.0))) + 1.0, curve(4.0), DELTA) //  (-1.0 * sqrt(4.0)) + 1.0 = -1.0

        assertEquals(0.0, curve(-1.0), DELTA) // -1.0 * (-1.0 * sqrt(1.0) - 1.0) = 0.0
        assertEquals(-1.0 * ((-1.0 * sqrt(abs(-4.0))) + 1.0), curve(-4.0), DELTA) // -1.0 * (-1.0 * sqrt(abs(4.0)) + 1.0) = 1.0
    }

    @Test
    fun `test radical curve with special values`() {
        val curve = RadicalCurve(1.0, 0.0)

        // Test with special values
        assertTrue(curve(Double.NaN).isNaN(), "NaN input should result in NaN output")
        assertTrue(curve(Double.POSITIVE_INFINITY).isInfinite(), "Infinity input should be handled")
        assertTrue(curve(Double.NEGATIVE_INFINITY).isInfinite(), "Negative Infinity input should be handled")
    }
}
