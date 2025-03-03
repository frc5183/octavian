package org.frc5183.math.curve

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.pow

class ExponentialCurveTest {
    private companion object {
        private const val DELTA = 0.0001
    }
    
    @Test
    fun `test exponential curve with positive base`() {
        val base = 3.0
        val curve = ExponentialCurve(base)

        assertEquals(0.0, curve(0.0), DELTA)

        assertEquals((((1 + base).pow(0.5) - 1) / base), curve(0.5), DELTA)
        assertEquals((((1 + base).pow(1.0) - 1) / base), curve(1.0), DELTA)

        assertEquals(-1 * (((1 + base).pow(0.5) - 1) / base), curve(-0.5), DELTA)
        assertEquals(-1 * (((1 + base).pow(1.0) - 1) / base), curve(-1.0), DELTA)
    }

    @Test
    fun `test exponential curve with different bases`() {
        val curve1 = ExponentialCurve(1.0)
        assertEquals(0.0, curve1(0.0), DELTA)
        assertEquals(1.0, curve1(1.0), DELTA)
        assertEquals(-1.0, curve1(-1.0), DELTA)

        val curve2 = ExponentialCurve(10.0)
        assertEquals(0.0, curve2(0.0), DELTA)
        assertEquals((((1 + 10.0).pow(1.0) - 1) / 10.0), curve2(1.0), DELTA)
        assertEquals(-1 * (((1 + 10.0).pow(1.0) - 1) / 10.0), curve2(-1.0), DELTA)
    }

    @Test
    fun `test exponential curve symmetry`() {
        val curve = ExponentialCurve(2.0)

        // For symmetric curves, f(-x) = -f(x)
        for (x in listOf(0.1, 0.5, 1.0, 1.5, 2.0)) {
            assertEquals(-curve(x), curve(-x), DELTA)
        }
    }

    @Test
    fun `test exponential curve with extreme inputs`() {
        val curve = ExponentialCurve(2.0)

        // Very small input
        val smallInput = 1e-6
        val expectedSmall = ((1 + 2.0).pow(smallInput) - 1) / 2.0
        assertEquals(expectedSmall, curve(smallInput), DELTA)

        // Very large input (be careful with overflow)
        val largeInput = 10.0
        val expectedLarge = ((1 + 2.0).pow(largeInput) - 1) / 2.0
        assertEquals(expectedLarge, curve(largeInput), DELTA)
    }

    @Test
    fun `test exponential curve with small base`() {
        val curve = ExponentialCurve(0.1)

        assertEquals(0.0, curve(0.0), DELTA)
        assertEquals((((1 + 0.1).pow(1.0) - 1) / 0.1), curve(1.0), DELTA)
        assertEquals(-1 * (((1 + 0.1).pow(1.0) - 1) / 0.1), curve(-1.0), DELTA)
    }
}
